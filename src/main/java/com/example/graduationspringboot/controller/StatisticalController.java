package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.service.StatisticalService;
import com.example.graduationspringboot.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/Statistical")
@Api(value = "统计接口", tags = "统计接口" )
public class StatisticalController {

    @Autowired
    StatisticalService statisticalService;

    /**
     * 查询今日统计数据
     * @return
     */
    @RequestMapping(value = "/todayStatistical", method = RequestMethod.GET)
    @ApiOperation(value = "查询今日统计数据",notes = "查询今日统计数据")
    public Result todayStatistical() {
        log.info("开始查询...");
        return statisticalService.todayStatistical();
    }

    /**
     * 查询历史统计数据
     * @return
     */
    @RequestMapping(value = "/historyStatistical", method = RequestMethod.GET)
    @ApiOperation(value = "查询历史统计数据",notes = "查询历史统计数据")
    public Result historyStatistical() {
        log.info("开始查询...");
        return statisticalService.historyStatistical();
    }

}
