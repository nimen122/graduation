package com.example.graduationspringboot.vo.resultChart;

import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import lombok.Data;

import java.util.List;

@Data
public class RResult {

    private List<Double> dataR;

    private List<Double> CLR;

    private List<Double> LCLR;

    private List<Double> UCLR;

    private List<ResultOfCriterion> criterionList;


}
