package com.example.graduationspringboot.vo.resultChart;

import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import lombok.Data;

import java.util.List;

@Data
public class CommonResult {

    private List<Double> data;

    private Double sigmaZ;

    private List<Double> CL;

    private List<Double> LCL;

    private List<Double> UCL;

    private List<ResultOfCriterion> criterionList;

}
