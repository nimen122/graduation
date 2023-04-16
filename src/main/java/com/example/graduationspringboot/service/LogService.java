package com.example.graduationspringboot.service;

import com.example.graduationspringboot.entity.Log;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.LogParam;

public interface LogService {

    Result getAllLog(LogParam logParam);

    boolean addLog(Log log);

    Result getLog(LogParam logParam);
}
