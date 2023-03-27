package com.example.graduationspringboot.service;

import com.example.graduationspringboot.entity.SysUser;

public interface SysUserService {

    /**
     * 根据账号查询用户
     */
    SysUser findUserByAccount(String account);

    SysUser findUser(String account, String password);

    void addUser(SysUser sysUser);
}
