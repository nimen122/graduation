package com.example.graduationspringboot.entity;

import lombok.Data;

@Data
public class Statistical {

    private int statisticalId;

    private String statisticalDate;  //统计数据日期

    private int statisticalImportData; // 当日对应的数据录入数

    private int statisticalErrorData;  //当日对应的上报异常数

}
