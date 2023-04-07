package com.example.graduationspringboot.utils;

import com.example.graduationspringboot.vo.calChartParams.LimitsOfChart;
import com.example.graduationspringboot.vo.resultChart.ChartResult;
import com.example.graduationspringboot.vo.resultChart.CommonResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Calculate {

    /**
     * 根据字符串数据和用户期望的分组大小将数据分组，方便后续计算各分组的均值和极差
     * @param data
     * @param groupSize
     * @return
     */
    public static List<List<BigDecimal>> splitGroup(List<Double> data,List<Integer> groupSize){
        List<List<BigDecimal>> group = new ArrayList<>();

        List<Boolean> isSelect  = new ArrayList<>();
        for (int i = 0;i<groupSize.size();i++){
            isSelect.add(false);
        }
        for (int  i =0;i<data.size();i++){
            if (!isSelect.get(i)){
                int currentGroupId = groupSize.get(i);
                List<BigDecimal> subGroup = new ArrayList<>();
                for (int j = 0;j<data.size();j++){
                    if (currentGroupId == groupSize.get(j)){
                        subGroup.add(new BigDecimal(data.get(j)+""));
                        isSelect.set(j,true);
                    }
                }
                group.add(subGroup);
            }
        }
        return group;
    }

    /**
     * 计算子组标准差
     * @param groupDataList
     * @param groupAve
     * @return
     */
    public static List<BigDecimal> GroupSigma(List<List<BigDecimal>> groupDataList, List<Double> groupAve){
        List<BigDecimal> groupSigma = new ArrayList<>();
        BigDecimal deviationAve = new BigDecimal("0.0");
        for (int i = 0; i < groupDataList.size(); i++) {
            BigDecimal deviationSum = new BigDecimal("0.0");
            for (int j = 0; j < groupDataList.get(i).size(); j++) {
                deviationSum = deviationSum.add((groupDataList.get(i).get(j).subtract(BigDecimal.valueOf(groupAve.get(i)))).multiply(groupDataList.get(i).get(j).subtract(BigDecimal.valueOf(groupAve.get(i)))));
            }
            deviationAve = deviationSum.divide(BigDecimal.valueOf(groupDataList.get(i).size()-1),4,BigDecimal.ROUND_HALF_UP) ;
            groupSigma.add(BigDecimal.valueOf(Math.sqrt(deviationAve.doubleValue())).setScale(4,BigDecimal.ROUND_HALF_UP));
        }
        return groupSigma;
    }

    /**
     * 计算分好组的各小组均值
     * @param groupDataList
     * @return
     */
    public static List<Double> GroupAveOfXbar(List<List<BigDecimal>> groupDataList) {
        List<Double> aveData = new ArrayList<>();
        for (int i = 0; i < groupDataList.size(); i++) {
            BigDecimal sum = new BigDecimal("0.0");
            for (int j = 0; j < groupDataList.get(i).size(); j++) {
                sum = sum.add(groupDataList.get(i).get(j));
            }
            BigDecimal ave = sum.divide(BigDecimal.valueOf(groupDataList.get(i).size()), 5, BigDecimal.ROUND_HALF_UP);
            aveData.add(ave.doubleValue());
        }
        return aveData;
    }

    /**
     * 计算Xbar中心线（CL）
     * @param data
     * @return
     */
    public static Double CLOfXbar(List<Double> data) {
        double aveData;
        Double sum = 0.0;
        for (int i = 0; i < data.size(); i++) {
            sum += data.get(i);
        }
        aveData = (BigDecimal.valueOf(sum/data.size()).setScale(4,BigDecimal.ROUND_HALF_UP)).doubleValue();
        return aveData;
    }

    /**
     * 计算Xbar中心线（CL）
     * @param splitData
     * @return
     */
    public static Double CLOfDelXbar(List<List<BigDecimal>> splitData) {
        double aveData;
        double sum = 0.0;
        int num = 0;
        for (int i = 0; i < splitData.size(); i++) {
            num += splitData.get(i).size();
            for (int j = 0;j<splitData.get(i).size();j++){
                sum += splitData.get(i).get(j).doubleValue();
            }
        }
        aveData = (BigDecimal.valueOf(sum/num).setScale(4,BigDecimal.ROUND_HALF_UP)).doubleValue();
        return aveData;
    }

    /**
     * 计算每个分组的极差
     * @param groupDataList
     * @return
     */
    public static List<Double> GroupRange(List<List<BigDecimal>> groupDataList) {
        List<Double> rangeList = new ArrayList<>();
        for (int i = 0; i < groupDataList.size(); i++) {
            BigDecimal groupMax = Collections.max(groupDataList.get(i));
            BigDecimal groupMin = Collections.min(groupDataList.get(i));
            Double range = groupMax.subtract(groupMin).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
            rangeList.add(range);
        }
        return rangeList;
    }

    /**
     * 使用Rbar方法估计西格玛值
     * @param groupDataList
     * @param groupRange
     * @return
     */
    public static double RbarSigma(List<List<BigDecimal>> groupDataList, List<Double> groupRange) {

        GlobalStatic globalStatic = new GlobalStatic();
        BigDecimal sumData = new BigDecimal("0");
        BigDecimal fiSum = new BigDecimal("0");
        for(int i = 0;i < groupDataList.size();i++){
            int n = groupDataList.get(i).size();
            BigDecimal f_i = BigDecimal.valueOf( (globalStatic.getd_2(n)*globalStatic.getd_2(n) ) /
                    (globalStatic.getd_3(n)*globalStatic.getd_2(n)) );
            sumData = sumData.add(
                    (f_i.multiply(BigDecimal.valueOf(groupRange.get(i))
                            .divide(BigDecimal.valueOf(globalStatic.getd_2(n)),4,BigDecimal.ROUND_HALF_UP)))
            );
            fiSum = fiSum.add(f_i);
        }
        return sumData.divide(fiSum,4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 使用Sbar方法(不使用无偏常量)估计西格玛值
     * @param groupDataList
     * @param groupAve
     * @return
     */
    public static double SbarSigmaWithoutConstant(List<List<BigDecimal>> groupDataList, List<Double> groupAve) {
        BigDecimal deviation;
        List<BigDecimal> groupSigma = Calculate.GroupSigma(groupDataList,groupAve);
        BigDecimal groupSigmaSum = new BigDecimal("0.0");
        for (int i = 0;i<groupSigma.size();i++){
            groupSigmaSum = groupSigmaSum.add(groupSigma.get(i));
        }
        deviation = BigDecimal.valueOf(groupSigmaSum.divide(BigDecimal.valueOf(groupAve.size()), 4, BigDecimal.ROUND_HALF_UP).doubleValue());
        return deviation.doubleValue();
    }

    /**
     * 使用Sbar方法(使用无偏常量)估计西格玛值
     * @param groupDataList
     * @param groupAve
     * @return
     */
    public static double SbarSigmaWithConstant(List<List<BigDecimal>> groupDataList, List<Double> groupAve) {
        GlobalStatic globalStatic = new GlobalStatic();
        BigDecimal result = new BigDecimal("0.0");
        BigDecimal h_iSum = new BigDecimal("0.0");
        List<BigDecimal> groupSigma = Calculate.GroupSigma(groupDataList,groupAve);
        for(int i = 0;i < groupDataList.size();i++){
            int n = groupDataList.get(i).size();
            BigDecimal h_i = BigDecimal.valueOf(globalStatic.getc_4(n)*globalStatic.getc_4(n)
                    /(1 - globalStatic.getc_4(n)*globalStatic.getc_4(n))) ;
            result = result.add(h_i.multiply(groupSigma.get(i)).divide(BigDecimal.valueOf(globalStatic.getc_4(n)),4,BigDecimal.ROUND_HALF_UP));
            h_iSum = h_iSum.add(h_i);
        }
        result = result.divide(h_iSum,4,BigDecimal.ROUND_HALF_UP);
        return result.doubleValue();
    }

    /**
     * 使用合并标准差的方法（不使用无偏常量）计算小组的方差，以此作为数据的西格玛估计值
     * @param groupDataList
     * @param groupAve
     * @return
     */
    public static double UnionSigmaWithoutConstant(List<List<BigDecimal>> groupDataList, List<Double> groupAve) {
        int groupSizeSum = 0;
        BigDecimal deviation;
        BigDecimal deviationSum = new BigDecimal("0.0");
        for (int i = 0; i < groupDataList.size(); i++) {
            groupSizeSum += groupDataList.get(i).size()-1;
            for (int j = 0; j < groupDataList.get(i).size(); j++) {
                deviationSum = deviationSum.add((groupDataList.get(i).get(j).subtract(BigDecimal.valueOf(groupAve.get(i)))).multiply(groupDataList.get(i).get(j).subtract(BigDecimal.valueOf(groupAve.get(i)))));
            }
        }
        deviation = BigDecimal.valueOf(Math.sqrt(deviationSum.divide(BigDecimal.valueOf(groupSizeSum), 5, BigDecimal.ROUND_HALF_UP).doubleValue()));
        return deviation.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 使用合并标准差的方法（使用无偏常量无偏估计）计算小组的方差，以此作为数据的西格玛估计值
     * @param groupDataList
     * @param groupAve
     * @return
     */
    public static double UnionSigmaWithConstant(List<List<BigDecimal>> groupDataList, List<Double> groupAve) {
        int groupSizeSum = 0;
        GlobalStatic globalStatic = new GlobalStatic();
        BigDecimal deviation;
        BigDecimal deviationSum = new BigDecimal("0.0");
        for (int i = 0; i < groupDataList.size(); i++) {
            groupSizeSum += groupDataList.get(i).size()-1;
            for (int j = 0; j < groupDataList.get(i).size(); j++) {
                deviationSum = deviationSum.add((groupDataList.get(i).get(j).subtract(BigDecimal.valueOf(groupAve.get(i)))).multiply(groupDataList.get(i).get(j).subtract(BigDecimal.valueOf(groupAve.get(i)))));
            }
        }
        deviation = BigDecimal.valueOf(Math.sqrt(deviationSum.divide(BigDecimal.valueOf(groupSizeSum), 5, BigDecimal.ROUND_HALF_UP).doubleValue()));
        deviation = deviation.divide(BigDecimal.valueOf(globalStatic.getc_4(groupSizeSum+1)),4,BigDecimal.ROUND_HALF_UP);
        return deviation.doubleValue();
    }

    /**
     * 计算Xbar控制图的UCL和LCL
     * @param groupData
     * @param cl
     * @param k
     * @param sigma
     * @return
     */
    public static LimitsOfChart UCLAndLCLOfXbar(List<List<BigDecimal>> groupData, double cl, int k, double sigma) {
        List<Double> UCLList = new ArrayList<>();
        List<Double> LCLList = new ArrayList<>();
        LimitsOfChart result = new LimitsOfChart();
        for (int i = 0;i<groupData.size();i++){
            BigDecimal UCLBuffer = BigDecimal.valueOf(cl + k*sigma/(Math.sqrt(groupData.get(i).size())));
            BigDecimal LCLBuffer = BigDecimal.valueOf(cl - k*sigma/(Math.sqrt(groupData.get(i).size())));
            UCLList.add(UCLBuffer.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
            LCLList.add(LCLBuffer.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setUCL(UCLList);
        result.setLCL(LCLList);
        return result;
    }

    /**
     * 计算R控制图的中心线和控制上下限
     * @param groupData
     * @param sigma
     * @param k
     * @return
     */
    public static LimitsOfChart LimitsOfR(List<List<BigDecimal>> groupData, double sigma, int k){
        List<Double> CL_RList = new ArrayList<>();
        List<Double> UCL_RList = new ArrayList<>();
        List<Double> LCL_RList = new ArrayList<>();
        LimitsOfChart result = new LimitsOfChart();
        GlobalStatic globalStatic =new GlobalStatic();
        for (int i = 0;i < groupData.size();i++){
            int n = groupData.get(i).size();
            BigDecimal cl = BigDecimal.valueOf(globalStatic.getd_2(n)*sigma);
            CL_RList.add(cl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal lcl = BigDecimal.valueOf(globalStatic.getd_2(n)*sigma - k*sigma*globalStatic.getd_3(n));
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            LCL_RList.add(lcl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal ucl = BigDecimal.valueOf(globalStatic.getd_2(n)*sigma + k*sigma*globalStatic.getd_3(n));
            UCL_RList.add(ucl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setCL(CL_RList);
        result.setLCL(LCL_RList);
        result.setUCL(UCL_RList);
        return result;
    }

    /**
     * 计算S控制图的中心线和控制上下限（使用无偏常量）
     * @param groupData
     * @param sigma
     * @param k
     * @return
     */
    public static LimitsOfChart LimitsOfSWithConstant(List<List<BigDecimal>> groupData, double sigma, int k){
        List<Double> CL_SList = new ArrayList<>();
        List<Double> UCL_SList = new ArrayList<>();
        List<Double> LCL_SList = new ArrayList<>();
        LimitsOfChart result = new LimitsOfChart();
        GlobalStatic globalStatic =new GlobalStatic();
        for (int i = 0;i < groupData.size();i++){
            int n = groupData.get(i).size();
            BigDecimal cl = BigDecimal.valueOf(globalStatic.getc_4(n)*sigma);
            CL_SList.add(cl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal lcl = BigDecimal.valueOf(globalStatic.getc_4(n)*sigma - k*sigma*globalStatic.getc_5(n));
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            LCL_SList.add(lcl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal ucl = BigDecimal.valueOf(globalStatic.getc_4(n)*sigma + k*sigma*globalStatic.getc_5(n));
            UCL_SList.add(ucl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setCL(CL_SList);
        result.setLCL(LCL_SList);
        result.setUCL(UCL_SList);
        return result;
    }

    /**
     * 计算S控制图的中心线和控制上下限（不使用无偏常量）
     * @param groupData
     * @param sigma
     * @param k
     * @return
     */
    public static LimitsOfChart LimitsOfSWithoutConstant(List<List<BigDecimal>> groupData, double sigma, int k){
        List<Double> CL_SList = new ArrayList<>();
        List<Double> UCL_SList = new ArrayList<>();
        List<Double> LCL_SList = new ArrayList<>();
        LimitsOfChart result = new LimitsOfChart();
        GlobalStatic globalStatic =new GlobalStatic();
        for (int i = 0;i < groupData.size();i++){
            int n = groupData.get(i).size();
            BigDecimal cl = BigDecimal.valueOf(sigma);
            CL_SList.add(cl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal lcl = BigDecimal.valueOf(sigma - k*(globalStatic.getc_5(n)/globalStatic.getc_4(n))*sigma);
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            LCL_SList.add(lcl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal ucl = BigDecimal.valueOf(sigma + k*(globalStatic.getc_5(n)/globalStatic.getc_4(n))*sigma);
            UCL_SList.add(ucl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setCL(CL_SList);
        result.setLCL(LCL_SList);
        result.setUCL(UCL_SList);
        return result;
    }

    /**
     * 通过移动极差长度对数据进行分组
     * @param sourceData
     * @param movingRangeLength
     * @return
     */
    public static List<List<BigDecimal>> splitMovingRangeGroup(List<Double> sourceData, int movingRangeLength) {
        List<List<BigDecimal>> result = new ArrayList<>();
        int step = movingRangeLength;
        while (step <= sourceData.size()) {
            List<BigDecimal> subGroupData = new ArrayList<>();
            for (int i = 0; i < movingRangeLength; i++) {
                subGroupData.add(BigDecimal.valueOf(sourceData.get(i + step - movingRangeLength))) ;
            }
            result.add(subGroupData);
            step ++;
        }
        return result;
    }

    /**
     * 计算移动极差平均值MRbar,作为西格玛的无偏估计
     * @param groupMovingRange
     * @param movingRangeLength
     * @return
     */
    public static double movingRangeAverageAsSigma(List<Double> groupMovingRange,int movingRangeLength){
        GlobalStatic globalStatic = new GlobalStatic();
        BigDecimal rangeSum = new BigDecimal("0.0");
        for(int i = 0;i<groupMovingRange.size();i++){
            rangeSum = rangeSum.add(BigDecimal.valueOf(groupMovingRange.get(i)));
        }
        BigDecimal rangeAve = rangeSum.divide(BigDecimal.valueOf(groupMovingRange.size()),4,BigDecimal.ROUND_HALF_UP);
        return rangeAve.divide(BigDecimal.valueOf(globalStatic.getd_2(movingRangeLength)),4,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 计算移动极差中位数,作为西格玛的无偏估计
     * @param groupMovingRange
     * @param movingRangeLength
     * @return
     */
    public static double movingRangeMedianAsSigma(List<Double> groupMovingRange,int movingRangeLength){
        GlobalStatic globalStatic = new GlobalStatic();
        Collections.sort(groupMovingRange);
        int size = groupMovingRange.size();
        double median;
        if (size % 2 != 1){
            median = (groupMovingRange.get(size/2-1)+groupMovingRange.get(size/2)+0.0)/2;
        }else {
            median = groupMovingRange.get((size-1)/2);
        }
        BigDecimal data = BigDecimal.valueOf(median/globalStatic.getd_4(movingRangeLength));
        data =data.setScale(4,BigDecimal.ROUND_HALF_UP);
        return data.doubleValue();
    }

    /**
     * 使用均方递差的平方根方法（使用无偏常量）估计西格玛
     * @param groupMovingRange :数据样本的移动极差
     * @param n :数据样本的个数
     * @return
     */
    public static double SRMSSDAsSigmaWithConstant(List<Double> groupMovingRange,int n){
        BigDecimal rangeSum = new BigDecimal("0.0");
        GlobalStatic globalStatic = new GlobalStatic();
        for (int i = 0;i<groupMovingRange.size();i++){
            rangeSum = rangeSum.add(BigDecimal.valueOf(groupMovingRange.get(i)*groupMovingRange.get(i)).setScale(4,BigDecimal.ROUND_HALF_UP));
        }
        rangeSum = BigDecimal.valueOf(0.5).multiply(rangeSum.divide(BigDecimal.valueOf(n-1),4,BigDecimal.ROUND_HALF_UP));
        double result = Math.sqrt(rangeSum.doubleValue())/globalStatic.getc_4p(n);
        rangeSum = BigDecimal.valueOf(result).setScale(4,BigDecimal.ROUND_HALF_UP);
        return rangeSum.doubleValue();
    }

    /**
     * 使用均方递差的平方根方法（不使用无偏常量）估计西格玛
     * @param groupMovingRange:数据样本的移动极差
     * @param n:数据样本的个数
     * @return
     */
    public static double SRMSSDAsSigmaWithoutConstant(List<Double> groupMovingRange,int n){
        BigDecimal rangeSum = new BigDecimal("0.0");
        for (int i = 0;i<groupMovingRange.size();i++){
            rangeSum = rangeSum.add(BigDecimal.valueOf(groupMovingRange.get(i)*groupMovingRange.get(i)));
        }
        rangeSum = BigDecimal.valueOf(0.5).multiply(rangeSum.divide(BigDecimal.valueOf(n-1),4,BigDecimal.ROUND_HALF_UP));
        double result = Math.sqrt(rangeSum.doubleValue());
        rangeSum = BigDecimal.valueOf(result).setScale(4,BigDecimal.ROUND_HALF_UP);
        return rangeSum.doubleValue();
    }

    /**
     * 计算MR控制图的中心线以及控制上下限
     * @param sigma
     * @param movingRangeLength:移动极差长度
     * @param k:k倍西格玛
     * @return
     */
    public static LimitsOfChart MRLimits(double sigma,int movingRangeLength,int k){
        LimitsOfChart result = new LimitsOfChart();
        GlobalStatic globalStatic = new GlobalStatic();
        BigDecimal cl = BigDecimal.valueOf(sigma).multiply(BigDecimal.valueOf(globalStatic.getd_2(movingRangeLength))).setScale(4,BigDecimal.ROUND_HALF_UP);
        BigDecimal lcl = BigDecimal.valueOf(globalStatic.getd_2(movingRangeLength) * sigma).subtract(BigDecimal.valueOf(k * globalStatic.getd_3(movingRangeLength) * sigma))
                .setScale(4,BigDecimal.ROUND_HALF_UP);
        if(lcl.compareTo(BigDecimal.ZERO) == -1){
            lcl = new BigDecimal("0");
        }
        BigDecimal ucl = BigDecimal.valueOf(globalStatic.getd_2(movingRangeLength) * sigma).add(BigDecimal.valueOf(k * globalStatic.getd_3(movingRangeLength) * sigma))
                .setScale(4,BigDecimal.ROUND_HALF_UP);
        List<Double> clList = new ArrayList<>();
        clList.add(cl.doubleValue());
        List<Double> lclList = new ArrayList<>();
        lclList.add(lcl.doubleValue());
        List<Double> uclList = new ArrayList<>();
        uclList.add(ucl.doubleValue());
        result.setCL(clList);
        result.setLCL(lclList);
        result.setUCL(uclList);
        return result;
    }

    /**
     * 计算p控制图的数据、中心线以及控制上下限
     * @param groupData
     * @param groupSize
     * @param historyRatio
     * @param k
     * @return
     */
    public static ChartResult resultDataOfP(List<Integer> groupData, List<Integer> groupSize, Double historyRatio, int k){

        ChartResult result = new ChartResult();
        List<Double> P_Data = new ArrayList<>();
        List<Double> P_CL = new ArrayList<>();
        List<Double> P_LCL = new ArrayList<>();
        List<Double> P_UCL = new ArrayList<>();
        int dataSum = 0;
        int groupSizeSum = 0;
        for (int i = 0;i<groupData.size();i++){
            dataSum += groupData.get(i);
            groupSizeSum += groupSize.get(i);
            P_Data.add(BigDecimal.valueOf(groupData.get(i)/(groupSize.get(i)+0.0)).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        double cl_p = 0;
        if (historyRatio != -1){
            cl_p = historyRatio;
        }else {
            cl_p = BigDecimal.valueOf(dataSum/(groupSizeSum+0.0)).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        double value = k*Math.sqrt(cl_p*(1-cl_p));
        for (int i = 0;i<groupData.size();i++){

            P_CL.add(cl_p);
            BigDecimal lcl = BigDecimal.valueOf(cl_p-k*Math.sqrt(cl_p*(1-cl_p)/(groupSize.get(i)+0.0)));
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            P_LCL.add(lcl.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());

            BigDecimal ucl = BigDecimal.valueOf(cl_p+k*Math.sqrt(cl_p*(1-cl_p)/(groupSize.get(i)+0.0)));
            if(ucl.compareTo(BigDecimal.ONE) == 1){
                ucl= new BigDecimal("1");
            }
            P_UCL.add(ucl.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
        }

        result.setChartData(P_Data);
        result.setChartCL(P_CL);
        result.setChartLCL(P_LCL);
        result.setChartUCL(P_UCL);
        return result;
    }

    /**
     * 计算Laney P'控制图的数据、中心线以及控制上下限
     * @param groupData
     * @param groupSize
     * @param historyRatio
     * @param k
     * @return
     */
    public static ChartResult resultDataOfLaneyP(List<Integer> groupData, List<Integer> groupSize, Double historyRatio, int k){

        ChartResult result = new ChartResult();
        List<Double> LaneyP_Data = new ArrayList<>();
        List<Double> LaneyP_LCL = new ArrayList<>();
        List<Double> LaneyP_UCL = new ArrayList<>();
        List<Double> LaneyP_CL = new ArrayList<>();
        List<Double> zList = new ArrayList<>();
        int dataSum = 0;
        int groupSizeSum = 0;
        for (int i = 0;i<groupData.size();i++){
            dataSum += groupData.get(i);
            groupSizeSum += groupSize.get(i);
            LaneyP_Data.add(BigDecimal.valueOf(groupData.get(i)/(groupSize.get(i)+0.0)).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        double cl_LaneyP =0;
        if (historyRatio != -1){
            cl_LaneyP = historyRatio;
        }else {
            cl_LaneyP = BigDecimal.valueOf(dataSum/(groupSizeSum+0.0)).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        for (int i = 0;i<groupData.size();i++){
            double flush = Math.sqrt(cl_LaneyP*(1-cl_LaneyP)/(groupSize.get(i)+0.0));
            zList.add((LaneyP_Data.get(i)-cl_LaneyP)/flush);
        }

        //使用平均移动极差方法计算sigma （计算得出的sigma即为要求的sigmaZ）
        List<List<BigDecimal>> z_data = Calculate.splitMovingRangeGroup(zList,2);
        List<Double> rangeList = Calculate.GroupRange(z_data);
        double MR = Calculate.movingRangeAverageAsSigma(rangeList,2);
        double sigmaZ = BigDecimal.valueOf(MR).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue(); //这里并没有除1.128，因为移动极差平均值估计的sigma=MRbar/d2(w),这里d2(2)=1.128
        for (int i = 0;i<groupData.size();i++){

            LaneyP_CL.add(cl_LaneyP);
            double flush = Math.sqrt(cl_LaneyP*(1-cl_LaneyP)/(groupSize.get(i)+0.0));
            BigDecimal lcl = BigDecimal.valueOf(cl_LaneyP-k*flush*sigmaZ);
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            LaneyP_LCL.add(lcl.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal ucl = BigDecimal.valueOf(cl_LaneyP+k*flush*sigmaZ);
            if(ucl.compareTo(BigDecimal.ONE) == 1){
                ucl= new BigDecimal("1");
            }
            LaneyP_UCL.add(ucl.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setSigmaZ(sigmaZ);
        result.setChartData(LaneyP_Data);
        result.setChartCL(LaneyP_CL);
        result.setChartLCL(LaneyP_LCL);
        result.setChartUCL(LaneyP_UCL);
        return result;
    }

    public static ChartResult resultDataOfNP(List<Integer> groupData, List<Integer> groupSize, Double historyRatio, int k){ //需检验data的size和groupSize的size是否一致

        ChartResult result = new ChartResult();
        List<Double> NP_CL = new ArrayList<>();
        List<Double> NP_LCL = new ArrayList<>();
        List<Double> NP_UCL = new ArrayList<>();
        List<Double> NP_Data = new ArrayList<>();
        int dataSum = 0;
        int groupSizeSum = 0;
        for (int i = 0;i<groupData.size();i++){
            dataSum += groupData.get(i);
            groupSizeSum += groupSize.get(i);
        }
        double p = 0;
        if (historyRatio != -1){
            p = historyRatio;
        }else {
            p = BigDecimal.valueOf(dataSum/(groupSizeSum+0.0)).doubleValue();
        }
        for (int i = 0;i<groupData.size();i++){
            NP_Data.add(Double.valueOf(groupData.get(i)));
            double flush = k*Math.sqrt(groupSize.get(i)*p*(1-p));
            NP_CL.add(BigDecimal.valueOf(groupSize.get(i)*p).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal lcl = BigDecimal.valueOf(groupSize.get(i)*p-flush);
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            NP_LCL.add(lcl.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());

            BigDecimal ucl = BigDecimal.valueOf(groupSize.get(i)*p+flush);
            if(ucl.compareTo(BigDecimal.valueOf(groupSize.get(i))) == 1){
                ucl= BigDecimal.valueOf(groupSize.get(i));
            }
            NP_UCL.add(ucl.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setChartData(NP_Data);
        result.setChartCL(NP_CL);
        result.setChartLCL(NP_LCL);
        result.setChartUCL(NP_UCL);
        return result;
    }

    /**
     * 计算U控制图的数据、中心线以及控制上下限
     * @param groupData
     * @param groupSize
     * @param historyParam
     * @param k
     * @return
     */
    public static ChartResult resultDataOfU(List<Integer> groupData, List<Integer> groupSize, Double historyParam, int k){
        ChartResult result = new ChartResult();
        List<Double> U_Data = new ArrayList<>();
        List<Double> U_CL = new ArrayList<>();
        List<Double> U_LCL = new ArrayList<>();
        List<Double> U_UCL = new ArrayList<>();
        int dataSum = 0;
        int groupSizeSum = 0;
        for (int i = 0;i<groupData.size();i++){
            dataSum += groupData.get(i);
            groupSizeSum += groupSize.get(i);
            U_Data.add(BigDecimal.valueOf(groupData.get(i)/(groupSize.get(i)+0.0)).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        double cl_u = 0;
        if (historyParam != -1){
            cl_u = historyParam;
        }else{
            cl_u = BigDecimal.valueOf(dataSum/(groupSizeSum+0.0)).setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        for (int i = 0;i<groupData.size();i++){
            U_CL.add(cl_u);
            double flush = Math.sqrt(cl_u/(groupSize.get(i)+0.0));
            BigDecimal lcl = BigDecimal.valueOf(cl_u-k*flush);
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            U_LCL.add(lcl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());

            BigDecimal ucl = BigDecimal.valueOf(cl_u+k*flush);
            U_UCL.add(ucl.setScale(4,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setChartData(U_Data);
        result.setChartCL(U_CL);
        result.setChartLCL(U_LCL);
        result.setChartUCL(U_UCL);
        return result;
    }

    /**
     * 计算Laney U'控制图的数据、中心线以及控制上下限
     * @param groupData
     * @param groupSize
     * @param historyParam
     * @param k
     * @return
     */
    public static ChartResult resultDataOfLaneyU(List<Integer> groupData, List<Integer> groupSize, Double historyParam, int k){

        ChartResult result = new ChartResult();
        List<Double> LaneyU_Data = new ArrayList<>();
        List<Double> LaneyU_CL = new ArrayList<>();
        List<Double> LaneyU_LCL = new ArrayList<>();
        List<Double> LaneyU_UCL = new ArrayList<>();
        List<Double> zList = new ArrayList<>();
        int dataSum = 0;
        int groupSizeSum = 0;
        for (int i = 0;i<groupData.size();i++){
            dataSum += groupData.get(i);
            groupSizeSum += groupSize.get(i);
            LaneyU_Data.add(BigDecimal.valueOf(groupData.get(i)/(groupSize.get(i)+0.0)).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        double cl_LaneyU = 0;
        if (historyParam != -1){
            cl_LaneyU = historyParam;
        }else {
            cl_LaneyU = BigDecimal.valueOf(dataSum/(groupSizeSum+0.0)).setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        for (int i = 0;i<groupData.size();i++){
            double flush = Math.sqrt(cl_LaneyU/(groupSize.get(i)+0.0));
            zList.add((LaneyU_Data.get(i)-cl_LaneyU)/flush);
        }
        //使用平均移动极差方法计算sigma （计算得出的sigma即为要求的sigmaZ）
        List<List<BigDecimal>> z_data = Calculate.splitMovingRangeGroup(zList,2);
        List<Double> rangeList = Calculate.GroupRange(z_data);
        double MR = Calculate.movingRangeAverageAsSigma(rangeList,2);
        double sigmaZ = BigDecimal.valueOf(MR).setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue(); //这里并没有除1.128，因为移动极差平均值估计的sigma=MRbar/d2(w),这里d2(2)=1.128
        for (int i = 0;i<groupData.size();i++){
            LaneyU_CL.add(cl_LaneyU);
            double flush =k*sigmaZ*Math.sqrt(cl_LaneyU/(groupSize.get(i)+0.0));
            BigDecimal lcl = BigDecimal.valueOf(cl_LaneyU-flush);
            if(lcl.compareTo(BigDecimal.ZERO) == -1){
                lcl= new BigDecimal("0");
            }
            LaneyU_LCL.add(lcl.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
            BigDecimal ucl = BigDecimal.valueOf(cl_LaneyU+flush);
            if(ucl.compareTo(BigDecimal.ONE) == 1){
                ucl= new BigDecimal("1");
            }
            LaneyU_UCL.add(ucl.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        result.setSigmaZ(sigmaZ);
        result.setChartData(LaneyU_Data);
        result.setChartCL(LaneyU_CL);
        result.setChartLCL(LaneyU_LCL);
        result.setChartUCL(LaneyU_UCL);
        return result;
    }

    /**
     * 计算C控制图的数据、中心线以及控制上下限
     * @param groupData
     * @param historyParam
     * @param k
     * @return
     */
    public static ChartResult resultDataOfC(List<Integer> groupData, Double historyParam, int k){

        ChartResult result = new ChartResult();
        int dataSum = 0;
        for (int i = 0;i<groupData.size();i++){
            dataSum += groupData.get(i);
        }
        double cl_c = 0;
        if (historyParam != -1){
            cl_c = historyParam;
        }else {
            cl_c = BigDecimal.valueOf(dataSum/(groupData.size()+0.0)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        }
        double flush = k*Math.sqrt(cl_c);
        double lcl_c = BigDecimal.valueOf(cl_c-flush).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        if (lcl_c < 0){
            lcl_c = 0;
        }
        double ucl_c = BigDecimal.valueOf(cl_c+flush).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
        List<Double> C_Data = new ArrayList<>();
        List<Double> C_CL = new ArrayList<>();
        List<Double> C_LCL = new ArrayList<>();
        List<Double> C_UCL = new ArrayList<>();
        for (int i=0;i<groupData.size();i++){
            C_Data.add(Double.valueOf(groupData.get(i)));
            C_CL.add(cl_c);
            C_LCL.add(lcl_c);
            C_UCL.add(ucl_c);
        }

        result.setChartData(C_Data);
        result.setChartCL(C_CL);
        result.setChartLCL(C_LCL);
        result.setChartUCL(C_UCL);
        return result;
    }


}
