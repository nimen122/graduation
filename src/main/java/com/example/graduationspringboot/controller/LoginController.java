package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.service.LoginService;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.GetUserParam;
import com.example.graduationspringboot.vo.params.LoginParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/Login")
@Api(value = "登录接口", tags = "登录接口" )
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 登录验证
     * @param loginParam
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录验证",notes = "登录验证")
    public Result login(@RequestBody LoginParam loginParam) {
        log.info("开始验证...");
        return loginService.login(loginParam);

    }

    /**
     * 查询全部用户信息
     * @return
     */
    @RequestMapping(value = "/allUser", method = RequestMethod.POST)
    @ApiOperation(value = "查询全部用户信息",notes = "查询全部用户信息")
    public Result allUser() {
        log.info("开始查询全部用户信息...");
        return loginService.allUser();
    }

    @RequestMapping(value = "/getUser", method = RequestMethod.POST)
    @ApiOperation(value = "根据条件查询用户信息",notes = "根据条件查询用户信息")
    public Result getUser(@RequestHeader("Authorization") String token,@RequestBody GetUserParam getUserParam) {
        log.info("开始根据条件查询用户信息...");
        return loginService.getUser(token,getUserParam);
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    @RequestMapping(value = "/currentUser", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户信息",notes = "获取用户信息")
    public Result currentUser(@RequestHeader("Authorization") String token) {

        return loginService.findUserByToken(token);
    }

    /**
     * 退出登录
     * @param token
     * @return
     */
    @RequestMapping(value = "/loginOut", method = RequestMethod.GET)
    @ApiOperation(value = "退出登录",notes = "退出登录")
    public Result loginOut(@RequestHeader("Authorization") String token) {

        return loginService.loginOut(token);
    }

    /**
     * 注册账号
     * @param loginParam
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ApiOperation(value = "注册账号",notes = "注册账号")
    public Result register(@RequestBody LoginParam loginParam) {
        return loginService.register(loginParam);
    }


    /**
     * 更新账号信息
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation(value = "更新账号信息",notes = "更新账号信息")
    public Result updateUser(@RequestHeader("Authorization") String token,@RequestBody SysUser sysUser) {
        return loginService.updateUser(token,sysUser);
    }


//    public Result testToken(@RequestHeader("Authorization") String token, @RequestBody LoginParam loginParam) {


}
