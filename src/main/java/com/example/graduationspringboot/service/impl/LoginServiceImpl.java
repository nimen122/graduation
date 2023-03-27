package com.example.graduationspringboot.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.service.LoginService;
import com.example.graduationspringboot.service.SysUserService;
import com.example.graduationspringboot.utils.JWTUtils;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.UserInfoVo;
import com.example.graduationspringboot.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

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
        System.out.println("password："+password);
        SysUser sysUser = sysUserService.findUser(account,password);



        if (sysUser == null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }

        System.out.println(sysUser.getUserRole());

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

}
