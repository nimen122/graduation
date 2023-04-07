package com.example.graduationspringboot.vo.params;

import lombok.Data;

import java.util.List;

@Data
public class DelAttributeChartParam {

    private List<Integer> groupData;

    private List<Integer> groupSize;

    private List<Integer> delPoint;

    private Double historyParam;

    private String rules;    //标记判异准则，chartAt为1表示选中对应规则

    private List<Integer> rulesKey;     //判异准则对应的Key值

}
