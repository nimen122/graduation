package com.example.graduationspringboot.vo;

import lombok.Data;

import java.util.List;

@Data
public class LogResultVo {

    private int total;

    private List<LogVo> logList;

}
