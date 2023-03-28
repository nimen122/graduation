package com.example.graduationspringboot;

import com.example.graduationspringboot.entity.Source;
import com.example.graduationspringboot.entity.Statistical;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.service.SourceService;
import com.example.graduationspringboot.service.StatisticalService;
import com.example.graduationspringboot.service.SysUserService;
import com.example.graduationspringboot.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

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

        redisUtil.addImportRedis(1);
        System.out.println(redisTemplate.opsForList().range(s,0,1));
    }
}
