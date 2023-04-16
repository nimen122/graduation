package com.example.graduationspringboot.mapper;

import com.example.graduationspringboot.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper {

    SysUser selectUserByAccount(String account);

    void addUser(SysUser sysUser);

    void updateUser(SysUser sysUser);

    List<SysUser> selectAllUser();

    List<SysUser> selectUser(String userAccount, String input, String userRole);
}
