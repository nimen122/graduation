package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.entity.Source;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.mapper.ChartDataMapper;
import com.example.graduationspringboot.mapper.SourceMapper;
import com.example.graduationspringboot.mapper.SysUserMapper;
import com.example.graduationspringboot.service.LoginService;
import com.example.graduationspringboot.service.SourceService;
import com.example.graduationspringboot.utils.RedisUtil;
import com.example.graduationspringboot.vo.ChartDataVo;
import com.example.graduationspringboot.vo.ErrorCode;
import com.example.graduationspringboot.vo.Result;
import com.example.graduationspringboot.vo.SourceVo;
import com.example.graduationspringboot.vo.params.ChartDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SourceServiceImpl implements SourceService {

    @Autowired
    SourceMapper sourceMapper;
    @Autowired
    ChartDataMapper chartDataMapper;
    @Autowired
    LoginService loginService;
    @Autowired
    SysUserMapper sysUserMapper;

    @Resource
    RedisUtil redisUtil;

    @Override
    public Result getSourceById(int sourceId) {

        Source source = sourceMapper.selectSourceById(sourceId);
        if (source != null)
            return Result.success(source);
        else return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());

    }

    @Override
    public Result addSource(String token, List<ChartDataParam> sourceParam) {
        /**
         * 1.增加数据首先需要在tl_source表中增加一条记录，通过keyProperty拿到新增数据的主码source_id（keyProperty为实体类对应的字段）
         * 2.在tl_chart_data表中批量增加用户上传的数据，并将返回的主码data_id进行字符串拼接
         * 3.将拼接好的字符串作为source的数据更新对应的source记录
         */
        SysUser sysUser = loginService.checkToken(token);
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = date.format(day);
        Source source = new Source();
        source.setUserAccount(sysUser.getUserAccount());
        source.setCollectTime(s);

        sourceMapper.insertSource(source);
        int currentSourceId = source.getSourceId();
        String sourceData = "";
        ChartData chartData = new ChartData();
        for (int i = 0; i< sourceParam.size()-1; i++){

            chartData.setDataName(sourceParam.get(i).getDataName());
            chartData.setDataValue(sourceParam.get(i).getDataValue());
            chartData.setBelongSource(currentSourceId);
            chartDataMapper.insertChartData(chartData);
            sourceData += chartData.getDataId()+" ";
        }
        chartData.setDataName(sourceParam.get(sourceParam.size()-1).getDataName());
        chartData.setDataValue(sourceParam.get(sourceParam.size()-1).getDataValue());
        chartData.setBelongSource(currentSourceId);
        chartDataMapper.insertChartData(chartData);
        sourceData += chartData.getDataId();

        source.setSourceId(currentSourceId);
        source.setSourceData(sourceData);
        sourceMapper.updateSource(source);
        redisUtil.addImportRedis(1);
        return Result.success(currentSourceId);
    }

    @Override
    public Result getChartData(String sourceData) {

        String[] split = sourceData.split(" ");
        List<ChartDataVo> chartDataVoList = new ArrayList<>();

        for (int i = 0;i<split.length;i++){
            ChartData chartData = chartDataMapper.selectDataById(Integer.parseInt(split[i]));
            ChartDataVo chartDataVo = new ChartDataVo();
            String[] chartDataSplit = chartData.getDataValue().split(" ");
            List<String> sList = new ArrayList<>();
            for (int j = 0;j<chartDataSplit.length;j++){
                sList.add(chartDataSplit[j]);
            }
            chartDataVo.setDataName(chartData.getDataName());
            chartDataVo.setDataValue(sList);
            chartDataVoList.add(chartDataVo);
        }
        return Result.success(chartDataVoList);
    }

    @Override
    public Result getAllSource() {
        List<SourceVo> sourceVoList = new ArrayList<>();
        List<Source> sourceList = new ArrayList<>();
        sourceList = sourceMapper.selectAllSource();
        for (int i =0;i<sourceList.size();i++){
            SourceVo sourceVo = new SourceVo();
            sourceVo.setSourceId(sourceList.get(i).getSourceId());
            sourceVo.setSourceData(sourceList.get(i).getSourceData());
            sourceVo.setUserAccount(sourceList.get(i).getUserAccount());
            sourceVo.setCollectTime(sourceList.get(i).getCollectTime());
            SysUser sysUser = sysUserMapper.selectUserByAccount(sourceList.get(i).getUserAccount());
            sourceVo.setUserName(sysUser.getUserName());
            sourceVoList.add(sourceVo);
        }


        return Result.success(sourceVoList);
    }
}
