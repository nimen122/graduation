package com.example.graduationspringboot.vo.resultChart;

import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import lombok.Data;

import java.util.List;

@Data
public class XbarSResult {

    private List<Double> dataXbar;

    private List<Double> CLXbar;

    private List<Double> LCLXbar;

    private List<Double> UCLXbar;

    private List<ResultOfCriterion> criterionListXbar;

    private List<Double> dataS;

    private List<Double> CLS;

    private List<Double> LCLS;

    private List<Double> UCLS;

    private List<ResultOfCriterion> criterionListS;

}
