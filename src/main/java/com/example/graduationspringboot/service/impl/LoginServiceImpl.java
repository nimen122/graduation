package com.example.graduationspringboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.mapper.SysUserMapper;
import com.example.graduationspringboot.service.LoginService;
import com.example.graduationspringboot.service.SysUserService;
import com.example.graduationspringboot.utils.JWTUtils;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.UserInfoVo;
import com.example.graduationspringboot.vo.UserResultVo;
import com.example.graduationspringboot.vo.params.GetUserParam;
import com.example.graduationspringboot.vo.params.LoginParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {


    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private LoginService loginService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    private static final String salt = "hfut2019215068";

    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去tl_user表中查询 是否存在
         * 3. 如果不存在 登录失败
         * 4. 如果存在 ，使用jwt 生成token 返回给前端
         * 5. token放入redis当中，redis  token：user信息 设置过期时间
         *  (登录认证的时候 先认证token字符串是否合法，去redis认证是否存在)
         */
        String account = loginParam.getLoginAccount();
        String password = loginParam.getLoginPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + salt);

        SysUser sysUser = sysUserService.findUser(account,password);

        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        String token = JWTUtils.createToken(sysUser.getUserAccount());

        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public Result findUserByToken(String token) {
        Result result = Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        if (StringUtils.isBlank(token)){
            return result;
        }
        Map<String,Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return  result;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_"+token);
        if (StringUtils.isBlank(token)){
            return  result;
        }
        SysUser sysUser = JSON.parseObject(userJson,SysUser.class);
        if (sysUser == null ){
            return result;
        }
        UserInfoVo userInfoVo = new UserInfoVo();

        userInfoVo.setUserAccount(sysUser.getUserAccount());
        userInfoVo.setUserRole(sysUser.getUserRole());
        userInfoVo.setUserName(sysUser.getUserName());
        userInfoVo.setUserPhone(sysUser.getUserPhone());
        userInfoVo.setToken(token);
        return Result.success(userInfoVo);
    }

    @Override
    public Result loginOut(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }

    @Override
    public Result register(LoginParam loginParam) {
        /**
         * 1. 判断参数 是否合法
         * 2. 判断账户是否存在，存在 返回账户已经被注册
         * 3. 不存在，注册用户
         * 4. 生成token
         * 5. 存入redis 并返回
         * 6. 注意 加上事务，一旦中间的任何过程出现问题，注册的用户 需要回滚
         */
        String account = loginParam.getLoginAccount();
        String password = loginParam.getLoginPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password) ){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser sysUser =  sysUserService.findUserByAccount(account);
        if (sysUser != null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(),"账户已经被注册了");
        }
        sysUser = new SysUser();
        sysUser.setUserAccount(account);
        sysUser.setUserPassword(DigestUtils.md5Hex(password + salt));
        sysUser.setUserRole("普通用户");
        sysUserService.addUser(sysUser);

        String token = JWTUtils.createToken(sysUser.getUserAccount());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(sysUser),1, TimeUnit.DAYS);
        return Result.success(token);
    }

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJson, SysUser.class);
        return sysUser;
    }

    @Override
    public Result updateUser(String token,SysUser sysUser) {
        /**
         * 1.token校验失败-》token非法
         * 2.要更新的账号不存在-》参数有误
         * 3.普通用户无法修改其他人的账号信息
         * 4.管理员无法修改同为管理员的账号信息
         * 5.普通用户不能修改名字和角色信息
         */
        SysUser currentUser = loginService.checkToken(token);

        if (currentUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        if (sysUser.getUserAccount() == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        SysUser updateUser = sysUserMapper.selectUserByAccount(sysUser.getUserAccount());
        if (updateUser == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        if (!currentUser.getUserAccount().equals(sysUser.getUserAccount())
                && currentUser.getUserRole().equals("普通用户")){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(),ErrorCode.NO_PERMISSION.getMsg());
        }
        if (!currentUser.getUserAccount().equals(sysUser.getUserAccount())
                && currentUser.getUserRole().equals(updateUser.getUserRole())){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(),ErrorCode.NO_PERMISSION.getMsg());
        }
        if (sysUser.getUserRole() != null && currentUser.getUserRole().equals("普通用户")){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(),ErrorCode.NO_PERMISSION.getMsg());
        }

        SysUser sysUser1 = new SysUser();
        sysUser1.setUserAccount(sysUser.getUserAccount());
        if (sysUser.getUserPassword()!=null){
            sysUser1.setUserPassword(DigestUtils.md5Hex(sysUser.getUserPassword() + salt));
        }
        if (sysUser.getUserPhone()!=null){
            sysUser1.setUserPhone(sysUser.getUserPhone());
        }
        if (sysUser.getUserName()!=null){
            sysUser1.setUserName(sysUser.getUserName());
        }
        if (sysUser.getUserRole()!=null){
            sysUser1.setUserRole(sysUser.getUserRole());
        }
        System.out.println(sysUser1);
        sysUserService.updateUser(sysUser1);
        return Result.success(null);
    }

    @Override
    public Result allUser() {
        List<SysUser> sysUserList = sysUserMapper.selectAllUser();
        System.out.println(sysUserList);
        for (int i = 0;i<sysUserList.size();i++){
            sysUserList.get(i).setUserPassword("");
        }
        return Result.success(sysUserList);
    }

    @Override
    public Result getUser(String token, GetUserParam getUserParam) {
        SysUser currentUser = loginService.checkToken(token);

        Page page =PageHelper.startPage(getUserParam.getPage(), getUserParam.getSize());
        List<SysUser> sysUserList = sysUserMapper.selectUser(currentUser.getUserAccount(),getUserParam.getInput(),getUserParam.getUserRole());
        UserResultVo userResultVo = new UserResultVo();
        userResultVo.setTotal((int) page.getTotal());
        userResultVo.setUserList(sysUserList);
        return Result.success(userResultVo);
    }

}
