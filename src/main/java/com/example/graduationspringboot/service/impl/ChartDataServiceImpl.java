package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.mapper.ChartDataMapper;
import com.example.graduationspringboot.service.ChartDataService;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChartDataServiceImpl implements ChartDataService {
    @Autowired
    private ChartDataMapper chartDataMapper;

    @Override
    public Result getDataById(int dataId) {

        ChartData chartData = chartDataMapper.selectDataById(dataId);
        if(chartData == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        return Result.success(chartData);
    }

//    @Override
//    public Result getDataByAccount(String account) {
//        List<ChartData> dataList = chartDataMapper.selectDataByAccount(account);
//        return Result.success(dataList);
//    }

    @Override
    public Result addChartData(ChartData chartData) {
        boolean flag = chartDataMapper.insertChartData(chartData);
        if (!flag){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }else {
            return Result.success(null);
        }
    }

    @Override
    public Result changeChartData(ChartData chartData) {
        boolean flag = chartDataMapper.updateChartData(chartData);
        if (!flag){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }else {
            return Result.success(null);
        }
    }

    @Override
    public Result delChartData(int dataId) {
        boolean flag = chartDataMapper.deleteChartData(dataId);
        if (!flag){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }else {
            return Result.success(null);
        }
    }

}
