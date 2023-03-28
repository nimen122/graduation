package com.example.graduationspringboot.service;

import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.ChartDataParam;

import java.util.List;

public interface SourceService {

    Result getSourceById(int sourceId);

    Result addSource(String token, List<ChartDataParam> sourceParam);

    Result getChartData(String sourceData);

    Result getAllSource();
}
