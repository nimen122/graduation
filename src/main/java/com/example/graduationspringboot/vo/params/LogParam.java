package com.example.graduationspringboot.vo.params;

import lombok.Data;

@Data
public class LogParam {

    private int page;

    private int size;

    private String input;

    private String logType;

    private String startTime;

    private String endTime;
}
