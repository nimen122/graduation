package com.example.graduationspringboot.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.service.LoginService;
import com.example.graduationspringboot.service.SourceService;
import com.example.graduationspringboot.vo.ChartDataVo;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.AddSourceParam;
import com.example.graduationspringboot.vo.params.ChartDataParam;
import com.example.graduationspringboot.vo.params.LoginParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(value = "/Source")
@Api(value = "数据源接口", tags = "数据源接口" )
public class SourceController {

    @Autowired
    SourceService sourceService;
    @Autowired
    LoginService loginService;

    /**
     * 查询所有数据源
     * @return
     */
    @RequestMapping(value = "/getAllSource", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有数据源",notes = "查询所有数据源")
    public Result getAllSource() {

        return sourceService.getAllSource();

    }

    /**
     * 根据id查询数据源
     * @param sourceId
     * @return
     */
    @RequestMapping(value = "/getSourceById", method = RequestMethod.POST)
    @ApiOperation(value = "根据id查询数据源",notes = "根据id查询数据源")
    public Result getSourceById(@RequestParam int sourceId) {

        return sourceService.getSourceById(sourceId);

    }

    /**
     * 新增数据源（上传一组数据）
     * @param dataJson
     * @return
     */
    @RequestMapping(value = "/addSource", method = RequestMethod.POST)
    @ApiOperation(value = "新增数据源",notes = "新增数据源")
    public Result addSource(@RequestHeader("Authorization") String token, @RequestBody String dataJson) {

        try{
            JSONObject jsonObject = JSONObject.parseObject(dataJson);
            List<ChartDataParam> sourceParam = JSONObject.parseArray(jsonObject.getJSONArray("dataJson").toJSONString(), ChartDataParam.class);
            return sourceService.addSource(token,sourceParam);
        }catch(Exception e){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
    }

    /**
     * 根据传入的sourceData字符串查询对应的数据项
     * @param sourceData
     * @return
     */
    @RequestMapping(value = "/getChartData", method = RequestMethod.POST)
    @ApiOperation(value = "查询对应的数据项",notes = "查询对应的数据项")
    public Result getChartData(@RequestParam String sourceData) {

        return sourceService.getChartData(sourceData);

    }

}
