package com.example.graduationspringboot.utils;


import com.example.graduationspringboot.entity.Statistical;
import com.example.graduationspringboot.service.StatisticalService;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class StatisticalRedis implements SchedulingConfigurer {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    StatisticalService statisticalService;
    @Resource
    RedisUtil redisUtil;

    @Autowired
    @SuppressWarnings("all")
    CronMapper cronMapper;

    @Mapper
    public interface CronMapper{
        @Select("select cron from tl_cron where cron_id = #{id}")
        public String getCron(int id);
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(() -> statisticalRedis(),
                triggerContext -> {
                    String cron = cronMapper.getCron(1);
                    if (cron.isEmpty()) {
                        System.out.println("cron is null");
                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }


    private void statisticalRedis() {
        System.out.println("基于接口定时任务: "+LocalDateTime.now());
        redisUtil.saveRedis();
    }
}
