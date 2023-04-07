package com.example.graduationspringboot.service;

import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.AttributeChartParam;
import com.example.graduationspringboot.vo.params.DelAttributeChartParam;

public interface AttributeChartService {

    Result P(AttributeChartParam attributeChartParam);

    Result LaneyP(AttributeChartParam attributeChartParam);

    Result NP(AttributeChartParam attributeChartParam);

    Result U(AttributeChartParam attributeChartParam);

    Result LaneyU(AttributeChartParam attributeChartParam);

    Result C(AttributeChartParam attributeChartParam);

    Result delP(DelAttributeChartParam attributeChartParam);

    Result delLaneyP(DelAttributeChartParam attributeChartParam);

    Result delNP(DelAttributeChartParam attributeChartParam);

    Result delU(DelAttributeChartParam attributeChartParam);

    Result delLaneyU(DelAttributeChartParam attributeChartParam);

    Result delC(DelAttributeChartParam attributeChartParam);
}
