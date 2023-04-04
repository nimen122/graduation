package com.example.graduationspringboot.vo.resultChart;

import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import lombok.Data;

import java.util.List;

@Data
public class XbarRResult {

    private List<Double> dataXbar;

    private List<Double> CLXbar;

    private List<Double> LCLXbar;

    private List<Double> UCLXbar;

    private List<ResultOfCriterion> criterionListXbar;

    private List<Double> dataR;

    private List<Double> CLR;

    private List<Double> LCLR;

    private List<Double> UCLR;

    private List<ResultOfCriterion> criterionListR;
}
