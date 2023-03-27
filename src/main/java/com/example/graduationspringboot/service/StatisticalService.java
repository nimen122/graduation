package com.example.graduationspringboot.service;

import com.example.graduationspringboot.entity.Statistical;
import com.example.graduationspringboot.vo.Result;

public interface StatisticalService {
    Result todayStatistical();

    Result historyStatistical();

    Result addStatistical(Statistical statistical);
}
