package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.mapper.SysUserMapper;
import com.example.graduationspringboot.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser findUserByAccount(String account) {
        return sysUserMapper.selectUserByAccount(account);
    }

    @Override
    public SysUser findUser(String account, String password) {
        SysUser sysUser = sysUserMapper.selectUserByAccount(account);
        if (sysUser == null){
            return null;
        }else if (sysUser.getUserPassword().equals(password)){
            return sysUser;
        }
        return null;
    }

    @Override
    public void addUser(SysUser sysUser) {
        sysUserMapper.addUser(sysUser);
    }
}
