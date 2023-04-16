package com.example.graduationspringboot.controller;

import com.example.graduationspringboot.service.LogService;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.LogParam;
import com.example.graduationspringboot.vo.params.LoginParam;
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
@RequestMapping(value = "/Log")
@Api(value = "日志接口", tags = "日志接口" )
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/getAllLog", method = RequestMethod.POST)
    @ApiOperation(value = "查询全部日志信息",notes = "查询全部日志信息")
    public Result getAllLog(@RequestBody LogParam logParam) {
        log.info("开始查询全部日志信息...");
        return logService.getAllLog(logParam);

    }

    @RequestMapping(value = "/getLog", method = RequestMethod.POST)
    @ApiOperation(value = "查询全部日志信息",notes = "查询全部日志信息")
    public Result getLog(@RequestBody LogParam logParam) {
        log.info("开始条件查询日志信息...");
        return logService.getLog(logParam);

    }

}
