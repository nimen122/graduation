package com.example.graduationspringboot.vo.params;

import lombok.Data;

@Data
public class SourceCondition {

    private int page;

    private int size;

    private String userAccount;

    private String dataState;

    private String startTime;

    private String endTime;

}
