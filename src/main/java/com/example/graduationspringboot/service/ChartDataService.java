package com.example.graduationspringboot.service;

import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.vo.Result;

public interface ChartDataService {

    Result getDataById(int dataId);

//    Result getDataByAccount(String account);

    Result addChartData(ChartData chartData);

    Result changeChartData(ChartData chartData);

    Result delChartData(int dataId);
}
