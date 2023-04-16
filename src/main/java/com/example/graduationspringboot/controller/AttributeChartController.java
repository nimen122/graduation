package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.service.AttributeChartService;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.AttributeChartParam;
import com.example.graduationspringboot.vo.params.DelAttributeChartParam;
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
@RequestMapping(value = "/AttributeChart")
@Api(value = "属性控制图接口", tags = "属性控制图接口" )
public class AttributeChartController {

    @Autowired
    AttributeChartService attributeChartService;

    @RequestMapping(value = "/P", method = RequestMethod.POST)
    @ApiOperation(value = "计算P控制图数据",notes = "计算P控制图数据")
    public Result P(@RequestBody AttributeChartParam attributeChartParam) {
        log.info("开始计算P控制图数据...");
        return attributeChartService.P(attributeChartParam);
    }

    @RequestMapping(value = "/LaneyP", method = RequestMethod.POST)
    @ApiOperation(value = "计算LaneyP控制图数据",notes = "计算LaneyP控制图数据")
    public Result LaneyP(@RequestBody AttributeChartParam attributeChartParam) {
        log.info("开始计算Laney P’控制图数据...");
        return attributeChartService.LaneyP(attributeChartParam);
    }

    @RequestMapping(value = "/NP", method = RequestMethod.POST)
    @ApiOperation(value = "计算LaneyP控制图数据",notes = "计算LaneyP控制图数据")
    public Result NP(@RequestBody AttributeChartParam attributeChartParam) {
        log.info("开始计算NP控制图数据...");
        return attributeChartService.NP(attributeChartParam);
    }

    @RequestMapping(value = "/U", method = RequestMethod.POST)
    @ApiOperation(value = "计算U控制图数据",notes = "计算U控制图数据")
    public Result U(@RequestBody AttributeChartParam attributeChartParam) {
        log.info("开始计算U控制图数据...");
        return attributeChartService.U(attributeChartParam);
    }

    @RequestMapping(value = "/LaneyU", method = RequestMethod.POST)
    @ApiOperation(value = "计算LaneyU控制图数据",notes = "计算LaneyU控制图数据")
    public Result LaneyU(@RequestBody AttributeChartParam attributeChartParam) {
        log.info("开始计算Laney U’控制图数据...");
        return attributeChartService.LaneyU(attributeChartParam);
    }

    @RequestMapping(value = "/C", method = RequestMethod.POST)
    @ApiOperation(value = "计算C控制图数据",notes = "计算C控制图数据")
    public Result C(@RequestBody AttributeChartParam attributeChartParam) {
        log.info("开始计算C控制图数据...");
        return attributeChartService.C(attributeChartParam);
    }

    @RequestMapping(value = "/delP", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算P控制图数据",notes = "重新计算P控制图数据")
    public Result delP(@RequestBody DelAttributeChartParam attributeChartParam) {
        log.info("开始重新计算P控制图数据...");
        return attributeChartService.delP(attributeChartParam);
    }

    @RequestMapping(value = "/delLaneyP", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算LaneyP控制图数据",notes = "重新计算LaneyP控制图数据")
    public Result delLaneyP(@RequestBody DelAttributeChartParam attributeChartParam) {
        log.info("开始重新计算LaneyP控制图数据...");
        return attributeChartService.delLaneyP(attributeChartParam);
    }

    @RequestMapping(value = "/delNP", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算NP控制图数据",notes = "重新计算NP控制图数据")
    public Result delNP(@RequestBody DelAttributeChartParam attributeChartParam) {
        log.info("开始重新计算NP控制图数据...");
        return attributeChartService.delNP(attributeChartParam);
    }

    @RequestMapping(value = "/delU", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算U控制图数据",notes = "重新计算U控制图数据")
    public Result delU(@RequestBody DelAttributeChartParam attributeChartParam) {
        log.info("开始重新计算U控制图数据...");
        return attributeChartService.delU(attributeChartParam);
    }

    @RequestMapping(value = "/delLaneyU", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算LaneyU控制图数据",notes = "重新计算LaneyU控制图数据")
    public Result delLaneyU(@RequestBody DelAttributeChartParam attributeChartParam) {
        log.info("开始重新计算LaneyU控制图数据...");
        return attributeChartService.delLaneyU(attributeChartParam);
    }

    @RequestMapping(value = "/delC", method = RequestMethod.POST)
    @ApiOperation(value = "重新计算C控制图数据",notes = "重新计算C控制图数据")
    public Result delC(@RequestBody DelAttributeChartParam attributeChartParam) {
        log.info("开始重新计算C控制图数据...");
        return attributeChartService.delC(attributeChartParam);
    }

}
