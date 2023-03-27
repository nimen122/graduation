package com.example.graduationspringboot.mapper;

import com.example.graduationspringboot.entity.Statistical;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StatisticalMapper {

    Statistical selectTodayStatistical(String s);

    List<Statistical> selectHistoryStatistical();

    boolean insertStatistical(Statistical statistical);

    boolean updateStatistical(Statistical statistical);

    int selectNum();

    boolean deleteStatistical(int minId);

    int selectMinId();
}
