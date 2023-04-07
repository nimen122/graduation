package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.service.SingleChartService;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.DelSingleChartParam;
import com.example.graduationspringboot.vo.params.SingleChartParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/SingleChart")
@Api(value = "单值的变量控制图接口", tags = "单值的变量控制图接口" )
public class SingleChartController {

    @Autowired
    SingleChartService singleChartService;

    @RequestMapping(value = "/IMR", method = RequestMethod.POST)
    @ApiOperation(value = "计算I-MR控制图数据",notes = "计算I-MR控制图数据")
    public Result IMR(@RequestBody SingleChartParam singleChartParam) {
        log.info("开始计算IMR控制图数据...");
        return singleChartService.IMR(singleChartParam);
    }

    @RequestMapping(value = "/I", method = RequestMethod.POST)
    @ApiOperation(value = "计算I控制图数据",notes = "计算I控制图数据")
    public Result I(@RequestBody SingleChartParam singleChartParam) {
        log.info("开始计算I控制图数据...");
        return singleChartService.I(singleChartParam);
    }

    @RequestMapping(value = "/MR", method = RequestMethod.POST)
    @ApiOperation(value = "计算MR控制图数据",notes = "计算MR控制图数据")
    public Result MR(@RequestBody SingleChartParam singleChartParam) {
        log.info("开始计算MR控制图数据...");
        return singleChartService.MR(singleChartParam);
    }

    @RequestMapping(value = "/delI", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算I控制图数据",notes = "重新计算I控制图数据")
    public Result delI(@RequestBody DelSingleChartParam delSingleChartParam) {
        log.info("开始重新计算I控制图数据...");
        return singleChartService.delI(delSingleChartParam);
    }

    @RequestMapping(value = "/delMR", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算MR控制图数据",notes = "重新计算MR控制图数据")
    public Result delMR(@RequestBody DelSingleChartParam delSingleChartParam) {
        log.info("开始重新计算MR控制图数据...");
        return singleChartService.delMR(delSingleChartParam);
    }

    @RequestMapping(value = "/delIMR", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算IMR控制图数据",notes = "重新计算IMR控制图数据")
    public Result delIMR(@RequestBody DelSingleChartParam delSingleChartParam) {
        log.info("开始重新计算IMR控制图数据...");
        return singleChartService.delIMR(delSingleChartParam);
    }

}
