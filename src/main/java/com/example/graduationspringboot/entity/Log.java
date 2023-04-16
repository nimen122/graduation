package com.example.graduationspringboot.entity;

import lombok.Data;

@Data
public class Log {

    private int logId;

    private String logAccount;

    private String logTime;

    private String logType;

    private int logContent;
}
