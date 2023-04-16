package com.example.graduationspringboot.mapper;

import com.example.graduationspringboot.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {
    List<Log> selectAllLog();

    void insertLog(Log log);

    List<Log> selectLog(String input, String logType, String startTime, String endTime);
}
