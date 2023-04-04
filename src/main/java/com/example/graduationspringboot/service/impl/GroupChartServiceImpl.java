package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.service.GroupChartService;
import com.example.graduationspringboot.utils.Calculate;
import com.example.graduationspringboot.utils.Criterion;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.calChartParams.LimitsOfChart;
import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import com.example.graduationspringboot.vo.params.GroupChartParam;
import com.example.graduationspringboot.vo.resultChart.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupChartServiceImpl implements GroupChartService {
    @Override
    public Result Xbar(GroupChartParam groupChartParam) {
        String rules = groupChartParam.getRules();
        List<Integer> rulesKey = groupChartParam.getRulesKey();
        //将数据根据子组ID分为多个子组
        List<List<BigDecimal>> splitGroupData  = Calculate.splitGroup(groupChartParam.getGroupData(), groupChartParam.getGroupSize());
        //计算各小组的均值
        List<Double> groupAve = Calculate.GroupAveOfXbar(splitGroupData);
        //计算各小组极差
        List<Double> groupRange = Calculate.GroupRange(splitGroupData);
        //计算Xbar控制图的中心线值
        double cl = 0.0;
        if (groupChartParam.getHistoryAve()!=null && groupChartParam.getHistoryAve()!=-1){
            cl = groupChartParam.getHistoryAve();
        }else {
            cl = Calculate.CLOfXbar(groupChartParam.getGroupData());
        }
        //将cl值填充为List
        List<Double> CL_Xbar = new ArrayList<>();
        for (int i=0;i<splitGroupData.size();i++){
            CL_Xbar.add(cl);
        }
        //计算Xbar的sigma
        double sigma = 0;
        if (groupChartParam.getHistorySigma()!=null && groupChartParam.getHistorySigma()!=-1){
            sigma = groupChartParam.getHistorySigma();
        }else {
            String sigmaMode = groupChartParam.getSigmaMode();
            if (sigmaMode.equals("RbarSigma")){
                sigma = Calculate.RbarSigma(splitGroupData,groupRange);
            }else if (sigmaMode.equals("SbarSigmaWithConstant")){
                sigma = Calculate.SbarSigmaWithConstant(splitGroupData,groupAve);
            }else if (sigmaMode.equals("SbarSigmaWithoutConstant")){
                sigma = Calculate.SbarSigmaWithoutConstant(splitGroupData,groupAve);
            }else if (sigmaMode.equals("UnionSigmaWithConstant")){
                sigma = Calculate.UnionSigmaWithConstant(splitGroupData,groupAve);
            }
            else if(sigmaMode.equals("UnionSigmaWithoutConstant")){
                sigma = Calculate.UnionSigmaWithoutConstant(splitGroupData,groupAve);
            }
        }
        //计算Xbar的UCL和LCL
        LimitsOfChart limitsOfChart = Calculate.UCLAndLCLOfXbar(splitGroupData,cl,rulesKey.get(0),sigma);
        List<Double> UCL_Xbar = limitsOfChart.getUCL();
        List<Double> LCL_Xbar = limitsOfChart.getLCL();
        //八条判异准则的校验
        List<ResultOfCriterion> resultOfCriterionList = Criterion.ResultOfCriterionOfXbar(groupAve,CL_Xbar,LCL_Xbar,UCL_Xbar,sigma,rules,rulesKey);

        List<ChartResult> resultList = new ArrayList<>();
        ChartResult XbarResult = new ChartResult();
        XbarResult.setChartType("Xbar");
        XbarResult.setChartData(groupAve);
        XbarResult.setChartCL(CL_Xbar);
        XbarResult.setChartUCL(UCL_Xbar);
        XbarResult.setChartLCL(LCL_Xbar);
        XbarResult.setChartCriterion(resultOfCriterionList);

        resultList.add(XbarResult);
        return Result.success(resultList);
    }

    @Override
    public Result R(GroupChartParam groupChartParam) {
        String rules = groupChartParam.getRules();
        List<Integer> rulesKey = groupChartParam.getRulesKey();
        //将数据根据子组ID分为多个子组
        List<List<BigDecimal>> splitGroupData  = Calculate.splitGroup(groupChartParam.getGroupData(), groupChartParam.getGroupSize());
        //计算各小组的均值
        List<Double> groupAve = Calculate.GroupAveOfXbar(splitGroupData);
        //计算各小组极差
        List<Double> groupRange = Calculate.GroupRange(splitGroupData);

        //计算R的sigma
        double sigma = 0;
        if (groupChartParam.getHistorySigma()!=null && groupChartParam.getHistorySigma()!=-1){
            sigma = groupChartParam.getHistorySigma();
        }else {
            String sigmaMode = groupChartParam.getSigmaMode();
            if (sigmaMode.equals("RbarSigma")){
                sigma = Calculate.RbarSigma(splitGroupData,groupRange);
            }else if (sigmaMode.equals("UnionSigmaWithConstant")){
                sigma = Calculate.UnionSigmaWithConstant(splitGroupData,groupAve);
            }
            else if(sigmaMode.equals("UnionSigmaWithoutConstant")){
                sigma = Calculate.UnionSigmaWithoutConstant(splitGroupData,groupAve);
            }
        }
        //计算R的中心线以及上下控制限
        LimitsOfChart limitsOfChart = Calculate.LimitsOfR(splitGroupData,sigma,rulesKey.get(0));
        List<Double> CL_R = limitsOfChart.getCL();
        List<Double> UCL_R = limitsOfChart.getUCL();
        List<Double> LCL_R = limitsOfChart.getLCL();
        //八条判异准则的校验
        List<ResultOfCriterion> resultOfCriterionList = Criterion.ResultOfCriterionOfRAndS(groupAve,CL_R,LCL_R,UCL_R,rules,rulesKey);

        List<ChartResult> resultList = new ArrayList<>();
        ChartResult RResult = new ChartResult();
        RResult.setChartType("R");
        RResult.setChartData(groupRange);
        RResult.setChartCL(CL_R);
        RResult.setChartUCL(UCL_R);
        RResult.setChartLCL(LCL_R);
        RResult.setChartCriterion(resultOfCriterionList);
        resultList.add(RResult);
        return Result.success(resultList);
    }

    @Override
    public Result S(GroupChartParam groupChartParam) {
        String rules = groupChartParam.getRules();
        List<Integer> rulesKey = groupChartParam.getRulesKey();
        //将数据根据子组ID分为多个子组
        List<List<BigDecimal>> splitGroupData  = Calculate.splitGroup(groupChartParam.getGroupData(), groupChartParam.getGroupSize());
        //计算各小组的均值
        List<Double> groupAve = Calculate.GroupAveOfXbar(splitGroupData);
        //计算各小组标准差
        List<BigDecimal> groupSigma = Calculate.GroupSigma(splitGroupData,groupAve);
        List<Double> data_S = new ArrayList<>();
        for (int i = 0;i<groupSigma.size();i++){
            data_S.add(groupSigma.get(i).doubleValue());
        }

        //计算S的sigma和控制界限
        double sigma = 0;
        LimitsOfChart limitsOfChart = new LimitsOfChart();
        if (groupChartParam.getHistorySigma()!=null && groupChartParam.getHistorySigma()!=-1){
            sigma = groupChartParam.getHistorySigma();
            limitsOfChart = Calculate.LimitsOfSWithConstant(splitGroupData,sigma, rulesKey.get(0));
        }else {
            String sigmaMode = groupChartParam.getSigmaMode();
            if (sigmaMode.equals("SbarSigmaWithConstant")){
                sigma = Calculate.SbarSigmaWithConstant(splitGroupData,groupAve);
                limitsOfChart = Calculate.LimitsOfSWithConstant(splitGroupData,sigma,rulesKey.get(0));
            }else if(sigmaMode.equals("SbarSigmaWithoutConstant")){
                sigma = Calculate.SbarSigmaWithoutConstant(splitGroupData,groupAve);
                limitsOfChart = Calculate.LimitsOfSWithoutConstant(splitGroupData,sigma,rulesKey.get(0));
            }else if(sigmaMode.equals("UnionSigmaWithConstant")){
                sigma = Calculate.UnionSigmaWithConstant(splitGroupData,groupAve);
                limitsOfChart = Calculate.LimitsOfSWithConstant(splitGroupData,sigma,rulesKey.get(0));
            }else if(sigmaMode.equals("UnionSigmaWithoutConstant")){
                sigma = Calculate.UnionSigmaWithoutConstant(splitGroupData,groupAve);
                limitsOfChart = Calculate.LimitsOfSWithoutConstant(splitGroupData,sigma,rulesKey.get(0));
            }
        }

        List<Double> CL_S = limitsOfChart.getCL();
        List<Double> UCL_S = limitsOfChart.getUCL();
        List<Double> LCL_S = limitsOfChart.getLCL();
        //八条判异准则的校验
        List<ResultOfCriterion> resultOfCriterionList = Criterion.ResultOfCriterionOfRAndS(groupAve,CL_S,LCL_S,UCL_S,rules,rulesKey);

        List<ChartResult> resultList = new ArrayList<>();
        ChartResult SResult = new ChartResult();
        SResult.setChartType("S");
        SResult.setChartData(data_S);
        SResult.setChartCL(CL_S);
        SResult.setChartUCL(UCL_S);
        SResult.setChartLCL(LCL_S);
        SResult.setChartCriterion(resultOfCriterionList);

        resultList.add(SResult);
        return Result.success(resultList);
    }

    @Override
    public Result XbarR(GroupChartParam groupChartParam) {
        String rules = groupChartParam.getRules();
        List<Integer> rulesKey = groupChartParam.getRulesKey();
        //将数据根据子组ID分为多个子组
        List<List<BigDecimal>> splitGroupData  = Calculate.splitGroup(groupChartParam.getGroupData(), groupChartParam.getGroupSize());
        //计算各小组的均值
        List<Double> groupAve = Calculate.GroupAveOfXbar(splitGroupData);
        //计算各小组极差
        List<Double> groupRange = Calculate.GroupRange(splitGroupData);

        //计算Xbar控制图的中心线值
        double cl = 0.0;
        if (groupChartParam.getHistoryAve()!=null && groupChartParam.getHistoryAve()!=-1){
            cl = groupChartParam.getHistoryAve();
        }else {
            cl = Calculate.CLOfXbar(groupChartParam.getGroupData());
        }
        //将cl值填充为List
        List<Double> CL_Xbar = new ArrayList<>();
        for (int i=0;i<splitGroupData.size();i++){
            CL_Xbar.add(cl);
        }
        //计算Xbar的sigma
        double sigma = 0;
        if (groupChartParam.getHistorySigma()!=null && groupChartParam.getHistorySigma()!=-1){
            sigma = groupChartParam.getHistorySigma();
        }else {
            String sigmaMode = groupChartParam.getSigmaMode();
            if (sigmaMode.equals("RbarSigma")){
                sigma = Calculate.RbarSigma(splitGroupData,groupRange);
            }else if (sigmaMode.equals("UnionSigmaWithConstant")){
                sigma = Calculate.UnionSigmaWithConstant(splitGroupData,groupAve);
            }
            else if(sigmaMode.equals("UnionSigmaWithoutConstant")){
                sigma = Calculate.UnionSigmaWithoutConstant(splitGroupData,groupAve);
            }
        }
        //计算Xbar的UCL和LCL
        LimitsOfChart limitsOfChartXbar = Calculate.UCLAndLCLOfXbar(splitGroupData,cl,rulesKey.get(0),sigma);
        List<Double> UCL_Xbar = limitsOfChartXbar.getUCL();
        List<Double> LCL_Xbar = limitsOfChartXbar.getLCL();
        //计算R的中心线以及上下控制限
        LimitsOfChart limitsOfChart = Calculate.LimitsOfR(splitGroupData,sigma,rulesKey.get(0));
        List<Double> CL_R = limitsOfChart.getCL();
        List<Double> UCL_R = limitsOfChart.getUCL();
        List<Double> LCL_R = limitsOfChart.getLCL();

        //Xbat八条判异准则的校验
        List<ResultOfCriterion> criterionListXbar = Criterion.ResultOfCriterionOfXbar(groupAve,CL_Xbar,LCL_Xbar,UCL_Xbar,sigma,rules,rulesKey);
        //R八条判异准则的校验
        List<ResultOfCriterion> criterionListR = Criterion.ResultOfCriterionOfRAndS(groupRange,CL_R,LCL_R,UCL_R,rules,rulesKey);

        List<ChartResult> resultList = new ArrayList<>();
        ChartResult XbarResult = new ChartResult();
        XbarResult.setChartType("Xbar");
        XbarResult.setChartData(groupAve);
        XbarResult.setChartCL(CL_Xbar);
        XbarResult.setChartUCL(UCL_Xbar);
        XbarResult.setChartLCL(LCL_Xbar);
        XbarResult.setChartCriterion(criterionListXbar);

        ChartResult RResult = new ChartResult();
        RResult.setChartType("R");
        RResult.setChartData(groupRange);
        RResult.setChartCL(CL_R);
        RResult.setChartUCL(UCL_R);
        RResult.setChartLCL(LCL_R);
        RResult.setChartCriterion(criterionListR);

        resultList.add(XbarResult);
        resultList.add(RResult);
        return Result.success(resultList);
    }

    @Override
    public Result XbarS(GroupChartParam groupChartParam) {
        String rules = groupChartParam.getRules();
        List<Integer> rulesKey = groupChartParam.getRulesKey();
        //将数据根据子组ID分为多个子组
        List<List<BigDecimal>> splitGroupData  = Calculate.splitGroup(groupChartParam.getGroupData(), groupChartParam.getGroupSize());
        //计算各小组的均值
        List<Double> groupAve = Calculate.GroupAveOfXbar(splitGroupData);
        //计算各小组极差
        List<Double> groupRange = Calculate.GroupRange(splitGroupData);
        //计算各小组标准差
        List<BigDecimal> groupSigma = Calculate.GroupSigma(splitGroupData,groupAve);

        //计算Xbar控制图的中心线值
        double cl = 0.0;
        if (groupChartParam.getHistoryAve()!=null && groupChartParam.getHistoryAve()!=-1){
            cl = groupChartParam.getHistoryAve();
        }else {
            cl = Calculate.CLOfXbar(groupChartParam.getGroupData());
        }
        //将cl值填充为List;将BigDecimal的S数据转化为Double
        List<Double> data_S = new ArrayList<>();
        List<Double> CL_Xbar = new ArrayList<>();
        for (int i=0;i<splitGroupData.size();i++){
            CL_Xbar.add(cl);
            data_S.add(groupSigma.get(i).doubleValue());
        }
        //计算Xbar的sigma;并计算S控制图的控制界限
        double sigma = 0;
        LimitsOfChart limitsOfChartS = new LimitsOfChart();
        if (groupChartParam.getHistorySigma()!=null && groupChartParam.getHistorySigma()!=-1){
            sigma = groupChartParam.getHistorySigma();
        }else {
            String sigmaMode = groupChartParam.getSigmaMode();
            limitsOfChartS = Calculate.LimitsOfSWithConstant(splitGroupData,sigma, rulesKey.get(0));
            if (sigmaMode.equals("SbarSigmaWithConstant")){
                sigma = Calculate.SbarSigmaWithConstant(splitGroupData,groupAve);
                limitsOfChartS = Calculate.LimitsOfSWithConstant(splitGroupData,sigma,rulesKey.get(0));
            }else if(sigmaMode.equals("SbarSigmaWithoutConstant")){
                sigma = Calculate.SbarSigmaWithoutConstant(splitGroupData,groupAve);
                limitsOfChartS = Calculate.LimitsOfSWithoutConstant(splitGroupData,sigma,rulesKey.get(0));
            }else if(sigmaMode.equals("UnionSigmaWithConstant")){
                sigma = Calculate.UnionSigmaWithConstant(splitGroupData,groupAve);
                limitsOfChartS = Calculate.LimitsOfSWithConstant(splitGroupData,sigma,rulesKey.get(0));
            }else if(sigmaMode.equals("UnionSigmaWithoutConstant")){
                sigma = Calculate.UnionSigmaWithoutConstant(splitGroupData,groupAve);
                limitsOfChartS = Calculate.LimitsOfSWithoutConstant(splitGroupData,sigma,rulesKey.get(0));
            }
        }
        List<Double> CL_S = limitsOfChartS.getCL();
        List<Double> UCL_S = limitsOfChartS.getUCL();
        List<Double> LCL_S = limitsOfChartS.getLCL();
        //计算Xbar的UCL和LCL
        LimitsOfChart limitsOfChartXbar = Calculate.UCLAndLCLOfXbar(splitGroupData,cl,rulesKey.get(0),sigma);
        List<Double> UCL_Xbar = limitsOfChartXbar.getUCL();
        List<Double> LCL_Xbar = limitsOfChartXbar.getLCL();
        //Xbar八条判异准则的校验
        List<ResultOfCriterion> criterionListXbar = Criterion.ResultOfCriterionOfXbar(groupAve,CL_Xbar,LCL_Xbar,UCL_Xbar,sigma,rules,rulesKey);
        //S八条判异准则的校验
        List<ResultOfCriterion> criterionListS = Criterion.ResultOfCriterionOfRAndS(data_S,CL_S,LCL_S,UCL_S,rules,rulesKey);

        List<ChartResult> resultList = new ArrayList<>();
        ChartResult XbarResult = new ChartResult();
        XbarResult.setChartType("Xbar");
        XbarResult.setChartData(groupAve);
        XbarResult.setChartCL(CL_Xbar);
        XbarResult.setChartUCL(UCL_Xbar);
        XbarResult.setChartLCL(LCL_Xbar);
        XbarResult.setChartCriterion(criterionListXbar);

        ChartResult SResult = new ChartResult();
        SResult.setChartType("S");
        SResult.setChartData(data_S);
        SResult.setChartCL(CL_S);
        SResult.setChartUCL(UCL_S);
        SResult.setChartLCL(LCL_S);
        SResult.setChartCriterion(criterionListS);

        resultList.add(XbarResult);
        resultList.add(SResult);
        return Result.success(resultList);
    }
}
