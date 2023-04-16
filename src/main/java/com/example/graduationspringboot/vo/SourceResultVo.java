package com.example.graduationspringboot.vo;

import lombok.Data;

import java.util.List;

@Data
public class SourceResultVo {

    private int total;

    private List<SourceVo> sourceList;
}
