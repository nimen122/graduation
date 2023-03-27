package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.service.impl.ChartDataServiceImpl;
import com.example.graduationspringboot.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/ChartData")
@Api(value = "数据接口", tags = "数据接口" )
public class ChartDataController {

    @Autowired
    private ChartDataServiceImpl chartDataService;

    /**
     * 通过id查询数据
     * @param dataId
     * @return
     */
    @RequestMapping(value = "/getDataByID", method = RequestMethod.GET)
    @ApiOperation(value = "查询数据",notes = "查询数据")
    public Result getDataByID(@RequestParam(value = "dataId") int dataId) {
        log.info("开始查询...");
        return chartDataService.getDataById(dataId);
    }

    /**
     * 通过账号查询数据（查询自己的数据）
     * @param account
     * @return
     */
    @RequestMapping(value = "/getDataByAccount", method = RequestMethod.GET)
    @ApiOperation(value = "通过账号查询数据",notes = "通过账号查询数据")
    public Result getDataByAccount(@RequestParam(value = "account") String account) {
        log.info("开始查询...");
        return chartDataService.getDataByAccount(account);
    }

    /**
     * 添加数据
     * @param chartData
     * @return
     */
    @RequestMapping(value = "/addChartData", method = RequestMethod.POST)
    @ApiOperation(value = "新增数据",notes = "新增数据")
    public Result addChartData(@RequestBody ChartData chartData) {
        log.info("开始新增...={}",chartData);
        return chartDataService.addChartData(chartData);
    }

    /**
     * 修改数据 （数据的id和账号字段不会修改）
     * @param chartData
     * @return
     */
    @RequestMapping(value = "/changeChartData", method = RequestMethod.POST)
    @ApiOperation(value = "修改数据", notes = "修改数据")
    public Result changeChartData(@RequestBody ChartData chartData) {
        log.info("开始修改...{}",chartData);
        return chartDataService.changeChartData(chartData);
    }

    /**
     * 删除数据
     * @param dataId
     * @return
     */
    @RequestMapping(value = "/delChartData", method = RequestMethod.POST)
    @ApiOperation(value = "删除数据", notes = "删除数据")
    public Result delChartData( @RequestParam(value = "dataId") int dataId) {
        log.info("开始删除...{}",dataId);
        return chartDataService.delChartData(dataId);
    }

}
