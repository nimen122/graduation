package com.example.graduationspringboot.vo.resultChart;

import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import lombok.Data;

import java.util.List;

@Data
public class ChartResult {

    private String chartType;

    private List<Double> chartData;

    private List<Double> chartCL;

    private List<Double> chartLCL;

    private List<Double> chartUCL;

    private List<ResultOfCriterion> chartCriterion;

    private Double sigmaZ;
}
