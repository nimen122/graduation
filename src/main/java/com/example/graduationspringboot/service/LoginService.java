package com.example.graduationspringboot.service;

import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.GetUserParam;
import com.example.graduationspringboot.vo.params.LoginParam;

public interface LoginService {

    Result login(LoginParam loginParam);

    Result findUserByToken(String token);

    Result loginOut(String token);

    Result register(LoginParam loginParam);

    SysUser checkToken(String token);

    Result updateUser(String token,SysUser sysUser);

    Result allUser();

    Result getUser(String token, GetUserParam getUserParam);
}
