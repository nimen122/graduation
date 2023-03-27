package com.example.graduationspringboot.mapper;

import com.example.graduationspringboot.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper {

    SysUser selectUserByAccount(String account);

    void addUser(SysUser sysUser);
}
