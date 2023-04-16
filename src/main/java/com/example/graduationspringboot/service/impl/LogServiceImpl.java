package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.entity.Log;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.mapper.LogMapper;
import com.example.graduationspringboot.mapper.SysUserMapper;
import com.example.graduationspringboot.service.LogService;
import com.example.graduationspringboot.vo.LogResultVo;
import com.example.graduationspringboot.vo.LogVo;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.params.LogParam;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;
    @Autowired
    private SysUserMapper sysUserMapper;



    @Override
    public Result getAllLog(LogParam logParam) {

        List<Log> logList = logMapper.selectAllLog();

        return Result.success(logList);
    }

    @Override
    public boolean addLog(Log log) {
        logMapper.insertLog(log);
        return true;
    }

    @Override
    public Result getLog(LogParam logParam) {

        Page page = PageHelper.startPage(logParam.getPage(), logParam.getSize());
        List<Log> logList = logMapper.selectLog(logParam.getInput(),logParam.getLogType(),logParam.getStartTime(),logParam.getEndTime());
        int total = (int) page.getTotal();
        List<LogVo> logVoList = new ArrayList<>();
        for (int i =0;i<logList.size();i++){
            Log log = logList.get(i);
            LogVo logVo = new LogVo();
            logVo.setLogId(log.getLogId());
            logVo.setLogAccount(log.getLogAccount());
            SysUser sysUser = sysUserMapper.selectUserByAccount(log.getLogAccount());
            logVo.setLogName(sysUser.getUserName());
            logVo.setLogType(log.getLogType());
            logVo.setLogTime(log.getLogTime());
            logVo.setLogContent(log.getLogContent());
            logVoList.add(logVo);
        }
        LogResultVo logResultVo = new LogResultVo();
        logResultVo.setTotal(total);
        logResultVo.setLogList(logVoList);
        return Result.success(logResultVo);
    }
}
