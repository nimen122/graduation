package com.example.graduationspringboot.mapper;

import com.example.graduationspringboot.entity.Source;
import com.example.graduationspringboot.vo.Result;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SourceMapper {

    Source selectSourceById(int sourceId);

    void insertSource(Source source);

    void updateSource(Source source);

    List<Source> selectAllSource();
}
