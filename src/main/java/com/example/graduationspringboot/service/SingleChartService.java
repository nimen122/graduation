package com.example.graduationspringboot.service;

import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.DelSingleChartParam;
import com.example.graduationspringboot.vo.params.SingleChartParam;

public interface SingleChartService {
    Result IMR(SingleChartParam singleChartParam);

    Result I(SingleChartParam singleChartParam);

    Result MR(SingleChartParam singleChartParam);

    Result delI(DelSingleChartParam delSingleChartParam);

    Result delMR(DelSingleChartParam delSingleChartParam);

    Result delIMR(DelSingleChartParam delSingleChartParam);
}
