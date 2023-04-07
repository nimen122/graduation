package com.example.graduationspringboot;

import com.example.graduationspringboot.entity.Source;
import com.example.graduationspringboot.entity.Statistical;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.service.SourceService;
import com.example.graduationspringboot.service.StatisticalService;
import com.example.graduationspringboot.service.SysUserService;
import com.example.graduationspringboot.utils.Calculate;
import com.example.graduationspringboot.utils.Criterion;
import com.example.graduationspringboot.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@SpringBootTest
public class BugTest {

    @Autowired
    SourceService sourceService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StatisticalService statisticalService;
    @Autowired
    SysUserService sysUserService;
    @Resource
    RedisUtil redisUtil;

    @Test
    void testlogin(){
        SysUser sysUser = sysUserService.findUserByAccount("admin");
        System.out.println(sysUser.getUserRole());
    }

    @Test
    void testAddSource(){
        Source source = new Source();
        source.setSourceData("1 5 9");
        source.setUserAccount("admin");
        source.setCollectTime("2023-3-25 12:00:00");
//        Result result = sourceService.addSource(source);
//        System.out.println(result.getData());
    }
    @Test
    void testSaveRedis(){

        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.format(day);
//        redisUtil.saveRedis();
        redisUtil.addImportRedis(1);
        System.out.println(redisTemplate.opsForList().range(s,0,-1));
    }

//    @Test
//    void testRedis(){
//        Date day = new Date();
//        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
//        String s = date.format(day);
////        System.out.println(s);
////        System.out.println(date);
//        List<Integer> list =new ArrayList<>();
//        if (redisTemplate.opsForList().range(s,0,1).size()==0){
//            redisTemplate.opsForList().rightPush(s,6);
//            redisTemplate.opsForList().rightPush(s,70);
//        }else {
//            list = redisTemplate.opsForList().range(s,0,1);
//            int i = list.get(0) +1;
//            list.set(0,i);
//            i = list.get(1) +1;
//            list.set(1,i);
//            redisTemplate.delete(s);
//            redisTemplate.opsForList().rightPush(s,list.get(0));
//            redisTemplate.opsForList().rightPush(s,list.get(1));
//        }
//        System.out.println(redisTemplate.opsForList().range(s,0,-1));
//
//    }

    @Test
    void testAddStatistical(){
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.format(day);
        Statistical statistical = new Statistical();
        statistical.setStatisticalDate(s);
        statistical.setStatisticalErrorData((Integer) redisTemplate.opsForList().index(s,0));
        statistical.setStatisticalImportData((Integer) redisTemplate.opsForList().index(s,1));
        System.out.println(statisticalService.addStatistical(statistical));
    }
    @Test
    void testRedis2(){
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.format(day);
//        System.out.println(redisTemplate.hasKey(s));
//        List<Integer> list = redisTemplate.opsForList().range(s,0,1);
//        int i = list.get(1) +3;
//        list.set(1,i);
//        redisTemplate.delete(s);
//        redisTemplate.opsForList().rightPush(s,list.get(0));
//        redisTemplate.opsForList().rightPush(s,list.get(1));
//        System.out.println(redisTemplate.opsForList().range(s,0,1));
//        redisTemplate.opsForList().rightPush(s,0);  //后保存今日录入数据
        redisUtil.addImportRedis(1);
        System.out.println(redisTemplate.opsForList().range(s,0,1));
    }

    @Test
    void testSplitGroup(){

        List<Double> dataList = new ArrayList(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
//        dataList.add(BigDecimal.valueOf(1));
//        dataList.add(BigDecimal.valueOf(2));
//        dataList.add(BigDecimal.valueOf(3));
//        dataList.add(BigDecimal.valueOf(4));
//        dataList.add(BigDecimal.valueOf(5));
//        dataList.add(BigDecimal.valueOf(6));
//        dataList.add(BigDecimal.valueOf(7));
//        dataList.add(BigDecimal.valueOf(8));
//        dataList.add(BigDecimal.valueOf(9));
//        dataList.add(BigDecimal.valueOf(10));
        List<Integer> grouSize = Arrays.asList(1,1,2,2,1,3,1,2,3,4);

        System.out.println(Calculate.splitGroup(dataList,grouSize));
    }
    @Test
    public void testXbarAve(){
        String datas = "601.6 602.8 598.4 598.2 600.8 600.8 600.4 598.2 599.4 601.2 602.2 601.6 599.8 603.8 600.8 598.0 601.6 602.4 601.4 601.2";
        List<Double> data = new ArrayList<>();
        List<Integer> groupSize = Arrays.asList(1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10);
        String[] split = datas.split(" ");
        for (int i = 0;i<split.length;i++){
            data.add(Double.valueOf(split[i]));
        }
        List<List<BigDecimal>> groupData = Calculate.splitGroup(data,groupSize);
        System.out.println(Calculate.GroupAveOfXbar(groupData));
        System.out.println(Calculate.CLOfXbar(data));
    }
    @Test
    public void testGroupRange(){
        String datas = "601.6 602.8 598.4 598.2 600.8 600.8 600.4 598.2 599.4 601.2 602.2 601.6 599.8 603.8 600.8 598.0 601.6 602.4 601.4 601.2";
        List<Double> data = new ArrayList<>();
        List<Integer> groupSize = Arrays.asList(1,1,1,2,2,2,3,3,3,4,4,4,5,5,5,6,6,6,7,7);
        String[] split = datas.split(" ");
        for (int i = 0;i<split.length;i++){
            data.add(Double.valueOf(split[i]));
        }
        List<List<BigDecimal>> groupData = Calculate.splitGroup(data,groupSize);

        System.out.println(Calculate.GroupRange(groupData));
    }
    @Test
    public void testSigma(){
        String datas = "601.6 602.8 598.4 598.2 600.8 600.8 600.4 598.2 599.4 601.2 602.2 601.6 599.8 603.8 600.8 598.0 601.6 602.4 601.4 601.2";
        List<Double> data = new ArrayList<>();
        List<Integer> groupSize = Arrays.asList(1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10);
        String[] split = datas.split(" ");
        for (int i = 0;i<split.length;i++){
            data.add(Double.valueOf(split[i]));
        }
        List<List<BigDecimal>> groupData = Calculate.splitGroup(data,groupSize);
        List<Double> groupRange = Calculate.GroupRange(groupData);
        List<Double> groupAve = Calculate.GroupAveOfXbar(groupData);
        double RbarSigma = Calculate.RbarSigma(groupData,groupRange);
        double SbarSigmaWithoutConstant = Calculate.SbarSigmaWithoutConstant(groupData,groupAve);
        double SbarSigmaWithConstant = Calculate.SbarSigmaWithConstant(groupData,groupAve);
        double UnionSigmaWithConstant = Calculate.UnionSigmaWithConstant(groupData,groupAve);
        double UnionSigmaWithoutConstant = Calculate.UnionSigmaWithoutConstant(groupData,groupAve);
        System.out.println(RbarSigma);
        System.out.println(SbarSigmaWithoutConstant);
        System.out.println(SbarSigmaWithConstant);
        System.out.println(UnionSigmaWithConstant);
        System.out.println(UnionSigmaWithoutConstant);
    }

    @Test
    public void testSplitMovingRangeGroup(){
        String datas = "601.6 602.8 598.4 598.2 600.8 600.8 600.4 598.2 599.4 601.2 602.2 601.6 599.8 603.8 600.8 598.0 601.6 602.4 601.4 601.2";
        List<Double> data = new ArrayList<>();
        List<Integer> groupSize = Arrays.asList(1,1,1,2,2,2,3,3,3,4,4,4,5,5,5,6,6,6,7,7);
        String[] split = datas.split(" ");
        for (int i = 0;i<split.length;i++){
            data.add(Double.valueOf(split[i]));
        }
//        List<List<BigDecimal>> groupData = Calculate.splitGroup(data,groupSize);
        List<List<BigDecimal>> splitData = Calculate.splitMovingRangeGroup(data,2);
        System.out.println(splitData.size());
        System.out.println(Calculate.GroupRange(splitData));
    }

    @Test
    public void testCriterion_5_2(){
        String datas = "600.4,600.8,599.6,602,598.6,597.2,598.2,599.4,598,599,599.8,600.2,602.8,603.6,600.2,598.4,603.4,602.2,599.2,604.2";
        List<Double> data = new ArrayList<>();
        String[] split = datas.split(",");
        for (int i = 0;i<split.length;i++){
            data.add(Double.valueOf(split[i]));
        }
//        List<List<BigDecimal>> groupData = Calculate.splitGroup(data,groupSize);
        List<List<BigDecimal>> splitData = Calculate.splitMovingRangeGroup(data,2);
        //计算每个分组的极差
        List<Double> groupMovingRange = Calculate.GroupRange(splitData);
        //计算单值控制图的CL
        double cl_SingleValue = Calculate.CLOfXbar(data);
        double sigma = Calculate.movingRangeAverageAsSigma(groupMovingRange,2);

        //计算单值控制图的上下限
        BigDecimal ucl_SingleValue = BigDecimal.valueOf(cl_SingleValue + 3 * sigma).setScale(4,BigDecimal.ROUND_HALF_UP) ;
        BigDecimal lcl_SingleValue = BigDecimal.valueOf(cl_SingleValue - 3 * sigma).setScale(4,BigDecimal.ROUND_HALF_UP);
        List<Double> CL_I = new ArrayList<>();
        List<Double> LCL_I = new ArrayList<>();
        List<Double> UCL_I = new ArrayList<>();
        for (int i=0;i<data.size();i++){
            CL_I.add(cl_SingleValue);
            LCL_I.add(lcl_SingleValue.doubleValue());
            UCL_I.add(ucl_SingleValue.doubleValue());
        }
        System.out.println(data);
        System.out.println(CL_I);
        System.out.println(sigma);
        System.out.println(Criterion.Criterion_6(data,CL_I,sigma,4));
    }
    @Test
    public void testDelGroupChartParam(){


        String datas = "600.4,600.8,599.6,602,598.6,597.2,598.2,599.4,598,599,599.8,600.2,602.8,603.6,600.2,598.4,603.4,602.2,599.2,604.2";
        List<Double> data = new ArrayList<>();
        String[] split = datas.split(",");
        for (int i = 0;i<split.length;i++){
            data.add(Double.valueOf(split[i]));
        }
        List<Integer> groupSize = Arrays.asList(1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9,10,10);
        //将数据根据子组ID分为多个子组
        List<List<BigDecimal>> splitGroupData  = Calculate.splitGroup(data, groupSize);
        //去除要删的组
        System.out.println(splitGroupData);
        List<Integer> delPoint = new ArrayList<>();
        delPoint.add(0);
        delPoint.add(1);
        Collections.sort(delPoint);

        for (int i = delPoint.size()-1;i>=0;i--){
            if (i <= splitGroupData.size()){
                System.out.println(delPoint.get(i));
                splitGroupData.remove((int)delPoint.get(i));
            }
        }

        System.out.println(splitGroupData);
    }


}
