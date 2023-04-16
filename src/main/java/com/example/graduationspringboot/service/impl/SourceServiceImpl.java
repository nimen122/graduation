package com.example.graduationspringboot.service.impl;

import com.example.graduationspringboot.entity.ChartData;
import com.example.graduationspringboot.entity.Log;
import com.example.graduationspringboot.entity.Source;
import com.example.graduationspringboot.entity.SysUser;
import com.example.graduationspringboot.mapper.ChartDataMapper;
import com.example.graduationspringboot.mapper.SourceMapper;
import com.example.graduationspringboot.mapper.SysUserMapper;
import com.example.graduationspringboot.service.LogService;
import com.example.graduationspringboot.service.LoginService;
import com.example.graduationspringboot.service.SourceService;
import com.example.graduationspringboot.utils.RedisUtil;
import com.example.graduationspringboot.vo.*;
import com.example.graduationspringboot.vo.params.ChartDataParam;
import com.example.graduationspringboot.vo.params.SourceCondition;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DateFormat;
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
    @Autowired
    LogService logService;

    @Resource
    RedisUtil redisUtil;

    private static final String commonPath = "http://localhost:8888/imgs/";

    @Override
    public Result getSourceById(int sourceId) {
        Source source = sourceMapper.selectSourceById(sourceId);
        SourceVo sourceVo = new SourceVo();
        sourceVo.setSourceId(source.getSourceId());
        sourceVo.setSourceData(source.getSourceData());
        sourceVo.setUserAccount(source.getUserAccount());
        sourceVo.setCollectTime(source.getCollectTime());
        sourceVo.setDataState(source.getDataState());
        sourceVo.setStateMsg(source.getStateMsg());
        List<String> imageList = new ArrayList<>();
        if (source.getStateImage() != null && !source.getStateImage().equals("")){
            String[] images = source.getStateImage().split(":");
            for (int j =0 ;j<images.length;j++){
                String imagePath = commonPath + images[j];
                imageList.add(imagePath);
            }
        }

        sourceVo.setStateImage(imageList);
        return Result.success(sourceVo);
//        if (source != null)
//            return Result.success(source);
//        else return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());

    }

    @Override
    public Result addSource(String token, List<ChartDataParam> sourceParam) {
        /**
         * 1.增加数据首先需要在tl_source表中增加一条记录，通过keyProperty拿到新增数据的主码source_id（keyProperty为实体类对应的字段）
         * 2.在tl_chart_data表中批量增加用户上传的数据，并将返回的主码data_id进行字符串拼接
         * 3.将拼接好的字符串作为source的数据更新对应的source记录
         */
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        if (sysUser.getUserAccount() == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = date.format(day);
        Source source = new Source();
        source.setUserAccount(sysUser.getUserAccount());
        source.setCollectTime(s);
        source.setDataState("未进行验证");
        source.setStateMsg("");
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
        sourceMapper.updateSourceData(source);
        redisUtil.addImportRedis(1);

        Log log = new Log();
        log.setLogAccount(sysUser.getUserAccount());
        log.setLogTime(s);
        log.setLogType("数据录入");
        log.setLogContent(currentSourceId);
        logService.addLog(log);
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
    public Result getDataById(int sourceId) {
        Source source = sourceMapper.selectSourceById(sourceId);
        if (source == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        String sourceData = source.getSourceData();
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
            sourceVo.setDataState(sourceList.get(i).getDataState());
            sourceVo.setStateMsg(sourceList.get(i).getStateMsg());
//            String imagesName = "";
            List<String> imageList = new ArrayList<>();
            if (sourceList.get(i).getStateImage() != null && !sourceList.get(i).getStateImage().equals("")){
                String[] images = sourceList.get(i).getStateImage().split(":");
                for (int j =0 ;j<images.length;j++){
                    String imagePath = commonPath + images[j];
//                    imagesName += imagePath + ":";
                    imageList.add(imagePath);
                }
//                imagesName = imagesName.substring(0,imagesName.length()-1);
            }
            sourceVo.setStateImage(imageList);
            SysUser sysUser = sysUserMapper.selectUserByAccount(sourceList.get(i).getUserAccount());
            sourceVo.setUserName(sysUser.getUserName());
            sourceVoList.add(sourceVo);
        }

        return Result.success(sourceVoList);
    }

    @Override
    public Result checkPass(String token, SourceStateVo sourceStateVo) {
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        if (sysUser.getUserAccount() == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Source source = new Source();
        source.setSourceId(sourceStateVo.getSourceId());
        source.setStateMsg(sourceStateVo.getStateMsg());
        source.setStateImage(sourceStateVo.getStateImage());
        source.setDataState("验证已通过");
        sourceMapper.updateSourceState(source);
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = date.format(day);
        Log log = new Log();
        log.setLogAccount(sysUser.getUserAccount());
        log.setLogTime(s);
        log.setLogType("验证通过");
        log.setLogContent(sourceStateVo.getSourceId());
        logService.addLog(log);
        return Result.success(null);
    }

    @Override
    public Result checkFail(String token, SourceStateVo sourceStateVo) {
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        if (sysUser.getUserAccount() == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Source source = new Source();
        source.setSourceId(sourceStateVo.getSourceId());
        source.setStateMsg(sourceStateVo.getStateMsg());
        source.setStateImage(sourceStateVo.getStateImage());
        source.setDataState("异常未处理");
        redisUtil.addErrorRedis(1);
        sourceMapper.updateSourceState(source);
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = date.format(day);
        Log log = new Log();
        log.setLogAccount(sysUser.getUserAccount());
        log.setLogTime(s);
        log.setLogType("上报异常");
        log.setLogContent(sourceStateVo.getSourceId());
        logService.addLog(log);
        return Result.success(null);
    }

    @Override
    public Result errorHandle(String token, SourceStateVo sourceStateVo) {
        SysUser sysUser = loginService.checkToken(token);
        if (sysUser == null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(),ErrorCode.TOKEN_ERROR.getMsg());
        }
        if (sysUser.getUserAccount() == null){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        if (sysUser.getUserRole().equals("普通用户")){
            return Result.fail(ErrorCode.NO_PERMISSION.getCode(),ErrorCode.NO_PERMISSION.getMsg());
        }
        Source source = new Source();
        source.setSourceId(sourceStateVo.getSourceId());
        source.setStateMsg(sourceStateVo.getStateMsg());
        source.setStateImage(sourceStateVo.getStateImage());
        source.setDataState("异常已解决");
        sourceMapper.updateSourceState(source);
        Date day = new Date();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = date.format(day);
        Log log = new Log();
        log.setLogAccount(sysUser.getUserAccount());
        log.setLogTime(s);
        log.setLogType("异常处理");
        log.setLogContent(sourceStateVo.getSourceId());
        logService.addLog(log);
        return Result.success(null);
    }

    @Override
    public Result getSource(String token, SourceCondition sourceCondition) {
        SysUser sysUser = loginService.checkToken(token);
        List<SourceVo> sourceVoList = new ArrayList<>();
        List<Source> sourceList = new ArrayList<>();
        int total = 0;
//        PageHelper.startPage(sourceCondition.getPage(), sourceCondition.getSize());
        if (sysUser.getUserRole().equals("普通用户")){
            Page page =PageHelper.startPage(sourceCondition.getPage(), sourceCondition.getSize());
            sourceList = sourceMapper.commonSelectSource(sourceCondition.getUserAccount(),sourceCondition.getDataState(),sourceCondition.getStartTime(),sourceCondition.getEndTime());
            total = (int) page.getTotal();
        }else {
            Page page =PageHelper.startPage(sourceCondition.getPage(), sourceCondition.getSize());
            sourceList = sourceMapper.manageSelectSource(sourceCondition.getUserAccount(),sourceCondition.getDataState(),sourceCondition.getStartTime(),sourceCondition.getEndTime());
            total = (int) page.getTotal();
        }
        for (int i =0;i<sourceList.size();i++){
            SourceVo sourceVo = new SourceVo();
            sourceVo.setSourceId(sourceList.get(i).getSourceId());
            sourceVo.setSourceData(sourceList.get(i).getSourceData());
            sourceVo.setUserAccount(sourceList.get(i).getUserAccount());
            sourceVo.setCollectTime(sourceList.get(i).getCollectTime());
            sourceVo.setDataState(sourceList.get(i).getDataState());
            sourceVo.setStateMsg(sourceList.get(i).getStateMsg());
            List<String> imageList = new ArrayList<>();
            if (sourceList.get(i).getStateImage() != null && !sourceList.get(i).getStateImage().equals("")){
                String[] images = sourceList.get(i).getStateImage().split(":");
                for (int j =0 ;j<images.length;j++){
                    String imagePath = commonPath + images[j];
//                    imagesName += imagePath + ":";
                    imageList.add(imagePath);
                }
//                imagesName = imagesName.substring(0,imagesName.length()-1);
            }
            sourceVo.setStateImage(imageList);
            SysUser sysUser2 = sysUserMapper.selectUserByAccount(sourceList.get(i).getUserAccount());
            sourceVo.setUserName(sysUser2.getUserName());
            sourceVoList.add(sourceVo);
        }
        SourceResultVo sourceResultVo = new SourceResultVo();

        sourceResultVo.setTotal(total);
        sourceResultVo.setSourceList(sourceVoList);

        return Result.success(sourceResultVo);
    }


}
