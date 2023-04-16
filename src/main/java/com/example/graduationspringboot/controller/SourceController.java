package com.example.graduationspringboot.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.service.LoginService;
import com.example.graduationspringboot.service.SourceService;
import com.example.graduationspringboot.vo.ChartDataVo;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.SourceStateVo;
import com.example.graduationspringboot.vo.params.AddSourceParam;
import com.example.graduationspringboot.vo.params.ChartDataParam;
import com.example.graduationspringboot.vo.params.LoginParam;
import com.example.graduationspringboot.vo.params.SourceCondition;
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
     * 分页条件查询数据
     * @return
     */
    @RequestMapping(value = "/getSource", method = RequestMethod.POST)
    @ApiOperation(value = "分页条件查询数据",notes = "分页条件查询数据")
    public Result getSource(@RequestHeader("Authorization") String token, @RequestBody SourceCondition sourceCondition) {
//        System.out.println(sourceCondition);
        return sourceService.getSource(token,sourceCondition);

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

    /**
     * 根据数据源ID查询数据项
     * @param sourceId
     * @return
     */
    @RequestMapping(value = "/getDataById", method = RequestMethod.POST)
    @ApiOperation(value = "根据数据源ID查询数据项",notes = "根据数据源ID查询数据项")
    public Result getDataById(@RequestParam int sourceId) {

        return sourceService.getDataById(sourceId);

    }

    /**
     * 数据验证通过
     * @param token
     * @param sourceStateVo
     * @return
     */
    @RequestMapping(value = "/checkPass", method = RequestMethod.POST)
    @ApiOperation(value = "验证通过",notes = "验证通过")
    public Result checkPass(@RequestHeader("Authorization") String token,@RequestBody SourceStateVo sourceStateVo) {

        return sourceService.checkPass(token,sourceStateVo);

    }

    /**
     * 上报异常
     * @param token
     * @param sourceStateVo
     * @return
     */
    @RequestMapping(value = "/checkFail", method = RequestMethod.POST)
    @ApiOperation(value = "上报异常",notes = "上报异常")
    public Result checkFail(@RequestHeader("Authorization") String token,@RequestBody SourceStateVo sourceStateVo) {

        return sourceService.checkFail(token,sourceStateVo);

    }

    /**
     * 异常处理
     * @param token
     * @param sourceStateVo
     * @return
     */
    @RequestMapping(value = "/errorHandle", method = RequestMethod.POST)
    @ApiOperation(value = "异常处理",notes = "异常处理")
    public Result errorHandle(@RequestHeader("Authorization") String token,@RequestBody SourceStateVo sourceStateVo) {

        return sourceService.errorHandle(token,sourceStateVo);

    }


}
