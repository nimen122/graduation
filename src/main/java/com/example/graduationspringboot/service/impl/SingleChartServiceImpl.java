package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.service.SingleChartService;
import com.example.graduationspringboot.utils.Calculate;
import com.example.graduationspringboot.utils.Criterion;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.calChartParams.LimitsOfChart;
import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import com.example.graduationspringboot.vo.params.SingleChartParam;
import com.example.graduationspringboot.vo.resultChart.ChartResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class SingleChartServiceImpl implements SingleChartService {

    @Override
    public Result IMR(SingleChartParam singleChartParam) {

        List<Double> singleData = singleChartParam.getSourceData();
        int movingRangeLength = singleChartParam.getMovingRangeLength();
        String sigmaMode = singleChartParam.getSigmaMode();
        int k = singleChartParam.getRulesKey().get(0);
        String rules = singleChartParam.getRules();
        List<Integer> rulesKey = singleChartParam.getRulesKey();
        //通过移动极差长度划分数据
        List<List<BigDecimal>> splitData = Calculate.splitMovingRangeGroup(singleData,movingRangeLength);
        //计算每个分组的极差
        List<Double> groupMovingRange = Calculate.GroupRange(splitData);
        //计算单值控制图的CL
        double cl_SingleValue = Calculate.CLOfXbar(singleData);
        //计算sigma
        double sigma = 0.0;
        if (sigmaMode.equals("movingRangeAverageAsSigma")){
            sigma = Calculate.movingRangeAverageAsSigma(groupMovingRange,movingRangeLength);
        }else if (sigmaMode.equals("movingRangeMedianAsSigma")){
            sigma = Calculate.movingRangeMedianAsSigma(groupMovingRange,movingRangeLength);
        }
        //计算单值控制图的上下限
        BigDecimal ucl_SingleValue = BigDecimal.valueOf(cl_SingleValue + k * sigma).setScale(4,BigDecimal.ROUND_HALF_UP) ;
        BigDecimal lcl_SingleValue = BigDecimal.valueOf(cl_SingleValue - k * sigma).setScale(4,BigDecimal.ROUND_HALF_UP);
        List<Double> CL_I = new ArrayList<>();
        List<Double> LCL_I = new ArrayList<>();
        List<Double> UCL_I = new ArrayList<>();
        for (int i=0;i<singleData.size();i++){
            CL_I.add(cl_SingleValue);
            LCL_I.add(lcl_SingleValue.doubleValue());
            UCL_I.add(ucl_SingleValue.doubleValue());
        }

        LimitsOfChart MRLimits = Calculate.MRLimits(sigma,movingRangeLength,k);
        List<Double> CL_MR = new ArrayList<>();
        List<Double> LCL_MR = new ArrayList<>();
        List<Double> UCL_MR = new ArrayList<>();
        for (int i =0;i<groupMovingRange.size();i++){
            CL_MR.add(MRLimits.getCL().get(0));
            LCL_MR.add(MRLimits.getLCL().get(0));
            UCL_MR.add(MRLimits.getUCL().get(0));
        }

        //I八条判异准则的校验
        List<ResultOfCriterion> criterionListI = Criterion.ResultOfCriterionOfI(singleData,CL_I,LCL_I,UCL_I,sigma,rules,rulesKey);
        //MR判异准则的校验
        List<ResultOfCriterion> criterionListMR = Criterion.ResultOfCriterionOfMR(groupMovingRange,CL_MR,LCL_MR,UCL_MR,rules,rulesKey);
        //为MR数据填充 -1, 向前端表示该点不绘制
        for (int i =0;i<movingRangeLength-1;i++){
            groupMovingRange.add(0, (double) -1);
            CL_MR.add(0, (double) -1);
            LCL_MR.add(0, (double) -1);
            UCL_MR.add(0, (double) -1);
        }

        List<ChartResult> resultList = new ArrayList<>();
        ChartResult IResult = new ChartResult();
        IResult.setChartType("I");
        IResult.setChartData(singleData);
        IResult.setChartCL(CL_I);
        IResult.setChartLCL(LCL_I);
        IResult.setChartUCL(UCL_I);
        IResult.setChartCriterion(criterionListI);

        ChartResult MRResult = new ChartResult();
        MRResult.setChartType("MR");
        MRResult.setChartData(groupMovingRange);
        MRResult.setChartCL(CL_MR);
        MRResult.setChartLCL(LCL_MR);
        MRResult.setChartUCL(UCL_MR);
        MRResult.setChartCriterion(criterionListMR);
        resultList.add(IResult);
        resultList.add(MRResult);
        return Result.success(resultList);
    }

    @Override
    public Result I(SingleChartParam singleChartParam) {
        List<Double> singleData = singleChartParam.getSourceData();
        int movingRangeLength = singleChartParam.getMovingRangeLength();
        String sigmaMode = singleChartParam.getSigmaMode();
        int k = singleChartParam.getRulesKey().get(0);
        String rules = singleChartParam.getRules();
        List<Integer> rulesKey = singleChartParam.getRulesKey();

        //通过移动极差长度划分数据
        List<List<BigDecimal>> splitData = Calculate.splitMovingRangeGroup(singleData,movingRangeLength);
        //计算每个分组的极差
        List<Double> groupMovingRange = Calculate.GroupRange(splitData);
        //计算单值控制图的CL
        double cl_SingleValue = Calculate.CLOfXbar(singleData);
        //计算sigma
        double sigma = 0.0;
        if (sigmaMode.equals("movingRangeAverageAsSigma")){
            sigma = Calculate.movingRangeAverageAsSigma(groupMovingRange,movingRangeLength);
        }else if (sigmaMode.equals("movingRangeMedianAsSigma")){
            sigma = Calculate.movingRangeMedianAsSigma(groupMovingRange,movingRangeLength);
        }else if (sigmaMode.equals("SRMSSDAsSigmaWithConstant")){
            sigma = Calculate.SRMSSDAsSigmaWithConstant(groupMovingRange,singleData.size());
        }else if (sigmaMode.equals("SRMSSDAsSigmaWithoutConstant")){
            sigma = Calculate.SRMSSDAsSigmaWithoutConstant(groupMovingRange,singleData.size());
        }
        //计算单值控制图的上下限
        BigDecimal ucl_SingleValue = BigDecimal.valueOf(cl_SingleValue + k * sigma).setScale(4,BigDecimal.ROUND_HALF_UP) ;
        BigDecimal lcl_SingleValue = BigDecimal.valueOf(cl_SingleValue - k * sigma).setScale(4,BigDecimal.ROUND_HALF_UP);
        List<Double> CL_I = new ArrayList<>();
        List<Double> LCL_I = new ArrayList<>();
        List<Double> UCL_I = new ArrayList<>();
        for (int i=0;i<singleData.size();i++){
            CL_I.add(cl_SingleValue);
            LCL_I.add(lcl_SingleValue.doubleValue());
            UCL_I.add(ucl_SingleValue.doubleValue());
        }
        List<ResultOfCriterion> criterionListI = Criterion.ResultOfCriterionOfI(singleData,CL_I,LCL_I,UCL_I,sigma,rules,rulesKey);

        List<ChartResult> resultList = new ArrayList<>();
        ChartResult IResult = new ChartResult();
        IResult.setChartType("I");
        IResult.setChartData(singleData);
        IResult.setChartCL(CL_I);
        IResult.setChartLCL(LCL_I);
        IResult.setChartUCL(UCL_I);
        IResult.setChartCriterion(criterionListI);
        resultList.add(IResult);

        return Result.success(resultList);
    }

    @Override
    public Result MR(SingleChartParam singleChartParam) {
        List<Double> singleData = singleChartParam.getSourceData();
        int movingRangeLength = singleChartParam.getMovingRangeLength();
        String sigmaMode = singleChartParam.getSigmaMode();
        int k = singleChartParam.getRulesKey().get(0);
        String rules = singleChartParam.getRules();
        List<Integer> rulesKey = singleChartParam.getRulesKey();
        //通过移动极差长度划分数据
        List<List<BigDecimal>> splitData = Calculate.splitMovingRangeGroup(singleData,movingRangeLength);
        //计算每个分组的极差
        List<Double> groupMovingRange = Calculate.GroupRange(splitData);
        double sigma = 0.0;
        if (sigmaMode.equals("movingRangeAverageAsSigma")){
            sigma = Calculate.movingRangeAverageAsSigma(groupMovingRange,movingRangeLength);
        }else if (sigmaMode.equals("movingRangeMedianAsSigma")){
            sigma = Calculate.movingRangeMedianAsSigma(groupMovingRange,movingRangeLength);
        }

        //计算MR控制图的控制限
        LimitsOfChart MRLimits = Calculate.MRLimits(sigma,movingRangeLength,k);
        List<Double> CL_MR = new ArrayList<>();
        List<Double> LCL_MR = new ArrayList<>();
        List<Double> UCL_MR = new ArrayList<>();
        for (int i =0;i<groupMovingRange.size();i++){
            CL_MR.add(MRLimits.getCL().get(0));
            LCL_MR.add(MRLimits.getLCL().get(0));
            UCL_MR.add(MRLimits.getUCL().get(0));
        }
        //MR判异准则的校验
        List<ResultOfCriterion> criterionListMR = Criterion.ResultOfCriterionOfMR(groupMovingRange,CL_MR,LCL_MR,UCL_MR,rules,rulesKey);
        //为MR数据填充 -1, 向前端表示该点不绘制
        for (int i =0;i<movingRangeLength-1;i++){
            groupMovingRange.add(0, (double) -1);
            CL_MR.add(0, (double) -1);
            LCL_MR.add(0, (double) -1);
            UCL_MR.add(0, (double) -1);
        }
        List<ChartResult> resultList = new ArrayList<>();
        ChartResult MRResult = new ChartResult();
        MRResult.setChartType("MR");
        MRResult.setChartData(groupMovingRange);
        MRResult.setChartCL(CL_MR);
        MRResult.setChartLCL(LCL_MR);
        MRResult.setChartUCL(UCL_MR);
        MRResult.setChartCriterion(criterionListMR);
        resultList.add(MRResult);

        return Result.success(resultList);
    }
}
