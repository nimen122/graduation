package com.example.graduationspringboot.vo;

import com.example.graduationspringboot.entity.SysUser;
import lombok.Data;

import java.util.List;

@Data
public class UserResultVo {

    private int total;

    private List<SysUser> userList;

}
