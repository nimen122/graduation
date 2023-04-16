package com.example.graduationspringboot.mapper;

import com.example.graduationspringboot.entity.Source;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SourceMapper {

    Source selectSourceById(int sourceId);

    void insertSource(Source source);

    void updateSourceData(Source source);

    List<Source> selectAllSource();

    void updateSourceState(Source source);

    List<Source> commonSelectSource(String userAccount, String dataState, String startTime, String endTime);

    List<Source> manageSelectSource(String userAccount, String dataState, String startTime, String endTime);
}
