package com.example.graduationspringboot.vo;

import com.example.graduationspringboot.entity.Statistical;
import lombok.Data;

import java.util.List;

@Data
public class HistoryStatisticalResultVo {

    private int historyImport;

    private int historyError;

    private List<Statistical> statisticalList;
}

