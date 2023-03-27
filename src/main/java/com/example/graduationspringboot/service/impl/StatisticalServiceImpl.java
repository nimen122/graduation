package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.entity.Statistical;
import com.example.graduationspringboot.mapper.StatisticalMapper;
import com.example.graduationspringboot.service.StatisticalService;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.HistoryStatisticalResultVo;
import com.example.graduationspringboot.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    @Autowired
    StatisticalMapper statisticalMapper;

    @Override
    public Result todayStatistical() {
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.format(day);
        Statistical statistical = statisticalMapper.selectTodayStatistical(s);
        if (statistical == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        return Result.success(statistical);
    }

    @Override
    public Result historyStatistical() {
        List<Statistical> statisticalList = new ArrayList<>();
        statisticalList = statisticalMapper.selectHistoryStatistical();
        int historyImportSum = 0;
        int historyErrorSum = 0;

        for (int i = 0;i<statisticalList.size();i++){
            historyImportSum += statisticalList.get(i).getStatisticalImportData();
            historyErrorSum += statisticalList.get(i).getStatisticalErrorData();
        }
        HistoryStatisticalResultVo historyStatistical = new HistoryStatisticalResultVo();
        historyStatistical.setStatisticalList(statisticalList);
        historyStatistical.setHistoryImport(historyImportSum);
        historyStatistical.setHistoryError(historyErrorSum);
        return Result.success(historyStatistical);
    }

    /**
     *
     * @param statistical
     * @return
     */
    @Override
    public Result addStatistical(Statistical statistical) {

        /**
         * 1.先查看表中是否已有相同的日期
         *      如果有，表明此时需要更新相同日期的对应数据
         *      如果没有，表明此时需要插入新的数据
         * 2.插入数据前，对表的条数进行查询，需求是保存7天的统计数据，超过7天的将被删除
         *      statisticalMapper.selectMinId() 实现获取最小id的值
         *      statisticalMapper.deleteStatistical(minId) 实现了删除自增id最小值的数据（最旧的数据）
         *
         */
        Statistical statistical1 = statisticalMapper.selectTodayStatistical(statistical.getStatisticalDate());
        if (statistical1 != null){
            boolean f = statisticalMapper.updateStatistical(statistical);
            if (f){
                return Result.success(null);
            }
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        int numStatistical = statisticalMapper.selectNum();
        if (numStatistical >=7){
            int minId = statisticalMapper.selectMinId();
            boolean f = statisticalMapper.deleteStatistical(minId);
            if (!f){
                return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
            }
        }
        boolean flag = statisticalMapper.insertStatistical(statistical);
        if (flag){
            return Result.success(null);
        }
        return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
    }
}
