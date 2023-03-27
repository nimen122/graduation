package com.example.graduationspringboot.mapper;

import com.example.graduationspringboot.entity.ChartData;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ChartDataMapper {

    ChartData selectDataById(int dataId);

    List<ChartData> selectDataByAccount(String account);

    boolean insertChartData(ChartData chartData);

    boolean deleteChartData(int dataId);

    boolean updateChartData(ChartData chartData);
}
