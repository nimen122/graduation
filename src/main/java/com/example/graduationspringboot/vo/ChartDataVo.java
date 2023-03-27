package com.example.graduationspringboot.vo;

import lombok.Data;

import java.util.List;

@Data
public class ChartDataVo {

    private String dataName;

    private List<String> dataValue;
}
