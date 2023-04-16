package com.example.graduationspringboot.service;

import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.SourceStateVo;
import com.example.graduationspringboot.vo.params.ChartDataParam;
import com.example.graduationspringboot.vo.params.SourceCondition;

import java.util.List;

public interface SourceService {

    Result getSourceById(int sourceId);

    Result addSource(String token, List<ChartDataParam> sourceParam);

    Result getChartData(String sourceData);

    Result getAllSource();

    Result checkPass(String token, SourceStateVo sourceStateVo);

    Result checkFail(String token, SourceStateVo sourceStateVo);

    Result errorHandle(String token, SourceStateVo sourceStateVo);

    Result getSource(String token, SourceCondition sourceCondition);

    Result getDataById(int sourceId);
}
