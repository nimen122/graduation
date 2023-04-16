package com.example.graduationspringboot.vo;

import lombok.Data;

import java.util.List;

@Data
public class SourceVo {

    private int sourceId;

    private String sourceData;

    private String userAccount;

    private String userName;

    private String collectTime;

    private String dataState;

    private String stateMsg;

    private List<String> stateImage;
}
