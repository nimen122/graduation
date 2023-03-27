package com.example.graduationspringboot.service;

import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.entity.Source;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.AddSourceParam;

import java.util.List;

public interface SourceService {

    Result getSourceById(int sourceId);

    Result addSource(String userAccount,AddSourceParam sourceParam);

    Result getChartData(String sourceData);

    Result getAllSource();
}
