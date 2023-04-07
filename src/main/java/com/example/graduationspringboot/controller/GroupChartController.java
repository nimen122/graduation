package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.service.GroupChartService;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.DelGroupChartParam;
import com.example.graduationspringboot.vo.params.GroupChartParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/GroupChart")
@Api(value = "子组的变量控制图接口", tags = "子组的变量控制图接口" )
public class GroupChartController {

    @Autowired
    GroupChartService groupChartService;

    @RequestMapping(value = "/Xbar", method = RequestMethod.POST)
    @ApiOperation(value = "计算Xbar控制图数据",notes = "计算Xbar控制图数据")
    public Result Xbar(@RequestBody GroupChartParam groupChartParam) {
        log.info("开始计算Xbar控制图数据...");
        return groupChartService.Xbar(groupChartParam);
    }

    @RequestMapping(value = "/R", method = RequestMethod.POST)
    @ApiOperation(value = "计算Xbar控制图数据",notes = "计算Xbar控制图数据")
    public Result R(@RequestBody GroupChartParam groupChartParam) {
        log.info("开始计算R控制图数据...");
        return groupChartService.R(groupChartParam);
    }

    @RequestMapping(value = "/S", method = RequestMethod.POST)
    @ApiOperation(value = "计算S控制图数据",notes = "计算S控制图数据")
    public Result S(@RequestBody GroupChartParam groupChartParam) {
        log.info("开始计算S控制图数据...");
        return groupChartService.S(groupChartParam);
    }

    @RequestMapping(value = "/XbarR", method = RequestMethod.POST)
    @ApiOperation(value = "计算XbarR控制图数据",notes = "计算XbarR控制图数据")
    public Result XbarR(@RequestBody GroupChartParam groupChartParam) {
        log.info("开始计算XbarR控制图数据...");
        return groupChartService.XbarR(groupChartParam);
    }

    @RequestMapping(value = "/XbarS", method = RequestMethod.POST)
    @ApiOperation(value = "计算XbarS控制图数据",notes = "计算XbarS控制图数据")
    public Result XbarS(@RequestBody GroupChartParam groupChartParam) {
        log.info("开始计算XbarS控制图数据...");
        return groupChartService.XbarS(groupChartParam);
    }

    @RequestMapping(value = "/delXbar", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算Xbar控制图数据",notes = "重新计算Xbar控制图数据")
    public Result delXbar(@RequestBody DelGroupChartParam delGroupChartParam) {
        log.info("重新计算Xbar控制图数据...");
        return groupChartService.delXbar(delGroupChartParam);
    }

    @RequestMapping(value = "/delR", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算R控制图数据",notes = "重新计算R控制图数据")
    public Result delR(@RequestBody DelGroupChartParam delGroupChartParam) {
        log.info("重新计算R控制图数据...");
        return groupChartService.delR(delGroupChartParam);
    }

    @RequestMapping(value = "/delS", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算S控制图数据",notes = "重新计算S控制图数据")
    public Result delS(@RequestBody DelGroupChartParam delGroupChartParam) {
        log.info("重新计算S控制图数据...");
        return groupChartService.delS(delGroupChartParam);
    }

    @RequestMapping(value = "/delXbarR", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算XbarR控制图数据",notes = "重新计算XbarR控制图数据")
    public Result delXbarR(@RequestBody DelGroupChartParam delGroupChartParam) {
        log.info("重新计算XbarR控制图数据...");
        return groupChartService.delXbarR(delGroupChartParam);
    }

    @RequestMapping(value = "/delXbarS", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算XbarS控制图数据",notes = "重新计算XbarS控制图数据")
    public Result delXbarS(@RequestBody DelGroupChartParam delGroupChartParam) {
        log.info("重新计算XbarS控制图数据...");
        return groupChartService.delXbarS(delGroupChartParam);
    }
}
