package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.entity.Source;
import com.example.graduationspringboot.mapper.ChartDataMapper;
import com.example.graduationspringboot.mapper.SourceMapper;
import com.example.graduationspringboot.service.SourceService;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.AddSourceParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SourceServiceImpl implements SourceService {

    @Autowired
    SourceMapper sourceMapper;
    @Autowired
    ChartDataMapper chartDataMapper;

    @Override
    public Result getSourceById(int sourceId) {

        Source source = sourceMapper.selectSourceById(sourceId);
        if (source != null)
            return Result.success(source);
        else return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());

    }

    @Override
    public Result addSource(String userAccount,AddSourceParam sourceParam) {
        /**
         * 1.增加数据首先需要在tl_source表中增加一条记录，通过keyProperty拿到新增数据的主码source_id（keyProperty为实体类对应的字段）
         * 2.在tl_chart_data表中批量增加用户上传的数据，并将返回的主码data_id进行字符串拼接
         * 3.将拼接好的字符串作为source的数据更新对应的source记录
         */

        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = date.format(day);
        Source source = new Source();
        source.setUserAccount(userAccount);
        source.setCollectTime(s);

        sourceMapper.insertSource(source);
        int currentSourceId = source.getSourceId();
        List<ChartData> chartDataList = sourceParam.getChartDataList();
        String sourceData = "";
        for (int i = 0;i<chartDataList.size()-1;i++){
            chartDataList.get(i).setBelongSource(currentSourceId);
            chartDataMapper.insertChartData(chartDataList.get(i));
            sourceData += chartDataList.get(i).getDataId()+" ";
        }
        chartDataList.get(chartDataList.size()-1).setBelongSource(currentSourceId);
        chartDataMapper.insertChartData(chartDataList.get(chartDataList.size()-1));
        sourceData += chartDataList.get(chartDataList.size()-1).getDataId();

        source.setSourceId(currentSourceId);
        source.setSourceData(sourceData);
        sourceMapper.updateSource(source);

        return Result.success(currentSourceId);
    }

    @Override
    public Result getChartData(String sourceData) {

        String[] split = sourceData.split(" ");
        List<ChartData> chartDataList = new ArrayList<>();
        for (int i = 0;i<split.length;i++){
            ChartData chartData = chartDataMapper.selectDataById(Integer.parseInt(split[i]));
            chartDataList.add(chartData);
        }
        return Result.success(chartDataList);
    }

    @Override
    public Result getAllSource() {

        List<Source> sourceList = new ArrayList<>();
        sourceList = sourceMapper.selectAllSource();

        return Result.success(sourceList);
    }
}
