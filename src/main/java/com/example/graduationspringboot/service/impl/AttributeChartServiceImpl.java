package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.service.AttributeChartService;
import com.example.graduationspringboot.utils.Calculate;
import com.example.graduationspringboot.utils.Criterion;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.calChartParams.ResultOfCriterion;
import com.example.graduationspringboot.vo.params.AttributeChartParam;
import com.example.graduationspringboot.vo.params.DelAttributeChartParam;
import com.example.graduationspringboot.vo.resultChart.ChartResult;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AttributeChartServiceImpl implements AttributeChartService {

    @Override
    public Result P(AttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);

        ChartResult PResult = Calculate.resultDataOfP(groupData,groupSize,attributeChartParam.getHistoryParam(),k);

        List<ResultOfCriterion> PCriterion = Criterion.ResultOfCriterionOfRAndS(PResult.getChartData(), PResult.getChartCL(), PResult.getChartLCL(),
                PResult.getChartUCL(), rules,rulesKey);

        PResult.setChartType("P");
        PResult.setChartCriterion(PCriterion);

        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(PResult);

        return Result.success(resultList);
    }

    @Override
    public Result LaneyP(AttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        ChartResult LaneyPResult = Calculate.resultDataOfLaneyP(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> LaneyPCriterion = Criterion.ResultOfCriterionOfRAndS(LaneyPResult.getChartData(), LaneyPResult.getChartCL(), LaneyPResult.getChartLCL(),
                LaneyPResult.getChartUCL(), rules,rulesKey);
        LaneyPResult.setChartType("Laney P‘");
        LaneyPResult.setChartCriterion(LaneyPCriterion);
        List<ChartResult> resultList = new ArrayList<>();

        resultList.add(LaneyPResult);
        return Result.success(resultList);
    }

    @Override
    public Result NP(AttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        ChartResult NPResult = Calculate.resultDataOfNP(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> NPCriterion = Criterion.ResultOfCriterionOfRAndS(NPResult.getChartData(), NPResult.getChartCL(), NPResult.getChartLCL(),
                NPResult.getChartUCL(), rules,rulesKey);
        NPResult.setChartType("NP");
        NPResult.setChartCriterion(NPCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(NPResult);
        return Result.success(resultList);

    }

    @Override
    public Result U(AttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        ChartResult UResult = Calculate.resultDataOfU(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> UCriterion = Criterion.ResultOfCriterionOfRAndS(UResult.getChartData(), UResult.getChartCL(), UResult.getChartLCL(),
                UResult.getChartUCL(), rules,rulesKey);
        UResult.setChartType("U");
        UResult.setChartCriterion(UCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(UResult);
        return Result.success(resultList);
    }

    @Override
    public Result LaneyU(AttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        ChartResult LaneyUResult = Calculate.resultDataOfLaneyU(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> LaneyCriterion = Criterion.ResultOfCriterionOfRAndS(LaneyUResult.getChartData(), LaneyUResult.getChartCL(), LaneyUResult.getChartLCL(),
                LaneyUResult.getChartUCL(), rules,rulesKey);
        LaneyUResult.setChartType("Laney U‘");
        LaneyUResult.setChartCriterion(LaneyCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(LaneyUResult);
        return Result.success(resultList);
    }

    @Override
    public Result C(AttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        ChartResult CResult = Calculate.resultDataOfC(groupData,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> CCriterion = Criterion.ResultOfCriterionOfRAndS(CResult.getChartData(), CResult.getChartCL(), CResult.getChartLCL(),
                CResult.getChartUCL(), rules,rulesKey);
        CResult.setChartType("C");
        CResult.setChartCriterion(CCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(CResult);
        return Result.success(resultList);
    }

    @Override
    public Result delP(DelAttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        //去除要删的组
        Collections.sort(attributeChartParam.getDelPoint());
        for (int i = attributeChartParam.getDelPoint().size()-1;i>=0;i--){
            if (i <= groupData.size()){
                groupData.remove((int)attributeChartParam.getDelPoint().get(i));
                groupSize.remove((int)attributeChartParam.getDelPoint().get(i));
            }
        }

        ChartResult PResult = Calculate.resultDataOfP(groupData,groupSize,attributeChartParam.getHistoryParam(),k);

        List<ResultOfCriterion> PCriterion = Criterion.ResultOfCriterionOfRAndS(PResult.getChartData(), PResult.getChartCL(), PResult.getChartLCL(),
                PResult.getChartUCL(), rules,rulesKey);

        PResult.setChartType("P");
        PResult.setChartCriterion(PCriterion);

        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(PResult);

        return Result.success(resultList);
    }

    @Override
    public Result delLaneyP(DelAttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        //去除要删的组
        Collections.sort(attributeChartParam.getDelPoint());
        for (int i = attributeChartParam.getDelPoint().size()-1;i>=0;i--){
            if (i <= groupData.size()){
                groupData.remove((int)attributeChartParam.getDelPoint().get(i));
                groupSize.remove((int)attributeChartParam.getDelPoint().get(i));
            }
        }

        ChartResult LaneyPResult = Calculate.resultDataOfLaneyP(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> LaneyPCriterion = Criterion.ResultOfCriterionOfRAndS(LaneyPResult.getChartData(), LaneyPResult.getChartCL(), LaneyPResult.getChartLCL(),
                LaneyPResult.getChartUCL(), rules,rulesKey);
        LaneyPResult.setChartType("Laney P‘");
        LaneyPResult.setChartCriterion(LaneyPCriterion);
        List<ChartResult> resultList = new ArrayList<>();

        resultList.add(LaneyPResult);
        return Result.success(resultList);
    }

    @Override
    public Result delNP(DelAttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        //去除要删的组
        Collections.sort(attributeChartParam.getDelPoint());
        for (int i = attributeChartParam.getDelPoint().size()-1;i>=0;i--){
            if (i <= groupData.size()){
                groupData.remove((int)attributeChartParam.getDelPoint().get(i));
                groupSize.remove((int)attributeChartParam.getDelPoint().get(i));
            }
        }
        ChartResult NPResult = Calculate.resultDataOfNP(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> NPCriterion = Criterion.ResultOfCriterionOfRAndS(NPResult.getChartData(), NPResult.getChartCL(), NPResult.getChartLCL(),
                NPResult.getChartUCL(), rules,rulesKey);
        NPResult.setChartType("NP");
        NPResult.setChartCriterion(NPCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(NPResult);
        return Result.success(resultList);
    }

    @Override
    public Result delU(DelAttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        //去除要删的组
        Collections.sort(attributeChartParam.getDelPoint());
        for (int i = attributeChartParam.getDelPoint().size()-1;i>=0;i--){
            if (i <= groupData.size()){
                groupData.remove((int)attributeChartParam.getDelPoint().get(i));
                groupSize.remove((int)attributeChartParam.getDelPoint().get(i));
            }
        }
        ChartResult UResult = Calculate.resultDataOfU(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> UCriterion = Criterion.ResultOfCriterionOfRAndS(UResult.getChartData(), UResult.getChartCL(), UResult.getChartLCL(),
                UResult.getChartUCL(), rules,rulesKey);
        UResult.setChartType("U");
        UResult.setChartCriterion(UCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(UResult);
        return Result.success(resultList);
    }

    @Override
    public Result delLaneyU(DelAttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        List<Integer> groupSize = attributeChartParam.getGroupSize();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        //去除要删的组
        Collections.sort(attributeChartParam.getDelPoint());
        for (int i = attributeChartParam.getDelPoint().size()-1;i>=0;i--){
            if (i <= groupData.size()){
                groupData.remove((int)attributeChartParam.getDelPoint().get(i));
                groupSize.remove((int)attributeChartParam.getDelPoint().get(i));
            }
        }
        ChartResult LaneyUResult = Calculate.resultDataOfLaneyU(groupData,groupSize,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> LaneyCriterion = Criterion.ResultOfCriterionOfRAndS(LaneyUResult.getChartData(), LaneyUResult.getChartCL(), LaneyUResult.getChartLCL(),
                LaneyUResult.getChartUCL(), rules,rulesKey);
        LaneyUResult.setChartType("Laney U‘");
        LaneyUResult.setChartCriterion(LaneyCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(LaneyUResult);
        return Result.success(resultList);
    }

    @Override
    public Result delC(DelAttributeChartParam attributeChartParam) {
        List<Integer> groupData = attributeChartParam.getGroupData();
        String rules = attributeChartParam.getRules();
        List<Integer> rulesKey = attributeChartParam.getRulesKey();
        int k = rulesKey.get(0);
        //去除要删的组
        Collections.sort(attributeChartParam.getDelPoint());
        for (int i = attributeChartParam.getDelPoint().size()-1;i>=0;i--){
            if (i <= groupData.size()){
                groupData.remove((int)attributeChartParam.getDelPoint().get(i));
            }
        }
        ChartResult CResult = Calculate.resultDataOfC(groupData,attributeChartParam.getHistoryParam(),k);
        List<ResultOfCriterion> CCriterion = Criterion.ResultOfCriterionOfRAndS(CResult.getChartData(), CResult.getChartCL(), CResult.getChartLCL(),
                CResult.getChartUCL(), rules,rulesKey);
        CResult.setChartType("C");
        CResult.setChartCriterion(CCriterion);
        List<ChartResult> resultList = new ArrayList<>();
        resultList.add(CResult);
        return Result.success(resultList);
    }

}
