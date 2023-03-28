package com.example.graduationspringboot.utils;

import com.example.graduationspringboot.entity.Statistical;
import com.example.graduationspringboot.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Component
public class RedisUtil {
    @Resource
    RedisTemplate redisTemplate;
    @Autowired
    StatisticalService statisticalService;

    public void addErrorRedis(int num){
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.format(day);
        if (!redisTemplate.hasKey(s)){
            redisTemplate.opsForList().rightPush(s,0);  //先保存今日上报异常
            redisTemplate.opsForList().rightPush(s,0);  //后保存今日录入数据
        }
        List<Integer> list = redisTemplate.opsForList().range(s,0,1);
        int i = list.get(0) +num;
        list.set(0,i);
        redisTemplate.delete(s);
        redisTemplate.opsForList().rightPush(s,list.get(0));
        redisTemplate.opsForList().rightPush(s,list.get(1));
        saveRedis();
    }

    public void addImportRedis(int num){
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.format(day);
        if (!redisTemplate.hasKey(s)){
            redisTemplate.opsForList().rightPush(s,0);  //先保存今日上报异常
            redisTemplate.opsForList().rightPush(s,0);  //后保存今日录入数据
        }
        List<Integer> list = redisTemplate.opsForList().range(s,0,1);
        int i = list.get(1) +num;
        list.set(1,i);
        redisTemplate.delete(s);
        redisTemplate.opsForList().rightPush(s,list.get(0));
        redisTemplate.opsForList().rightPush(s,list.get(1));
        saveRedis();

    }

    public void saveRedis() {
//        System.out.println("基于接口定时任务: "+ LocalDateTime.now());
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String s = date.format(day);
        if (!redisTemplate.hasKey(s)){
            redisTemplate.opsForList().rightPush(s,0);  //先保存今日上报异常
            redisTemplate.opsForList().rightPush(s,0);  //后保存今日录入数据
        }
        Statistical statistical = new Statistical();
        statistical.setStatisticalDate(s);
        statistical.setStatisticalErrorData((Integer) redisTemplate.opsForList().index(s,0));
        statistical.setStatisticalImportData((Integer) redisTemplate.opsForList().index(s,1));
        statisticalService.addStatistical(statistical);
    }

}