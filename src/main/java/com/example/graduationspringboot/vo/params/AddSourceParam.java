package com.example.graduationspringboot.vo.params;

import com.example.graduationspringboot.entity.ChartData;
import lombok.Data;

import java.util.List;

@Data
public class AddSourceParam {

//    private String userAccount;

    private List<ChartData> chartDataList;
}
