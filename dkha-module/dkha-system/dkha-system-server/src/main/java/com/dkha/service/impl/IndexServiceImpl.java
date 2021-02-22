package com.dkha.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.rabbitmq.MQQueueNameConfig;
import com.dkha.commons.tools.redis.RedisUtils;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.commons.tools.utils.Result;
import com.dkha.dao.*;
import com.dkha.dto.*;
import com.dkha.entity.*;
import com.dkha.enums.AlarmTypeEnum;
import com.dkha.enums.SexTypeEnum;
import com.dkha.feign.SysAdminFeignClient;
import com.dkha.service.IndexService;
import com.dkha.websocket.WebSocketServer;
import com.dkha.websocket.data.MessageData;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author xiedong
 * @version v1.0
 * @date 2020-08-29 10:22
 */

@Service
@Slf4j
@EnableScheduling
public class IndexServiceImpl implements IndexService {

    @Autowired
    private ScTransformalarmDao scTransformalarmDao;

    @Autowired
    private ScUwbalarmDao scUwbalarmDao;

    @Autowired
    private ScPmalarmDao scPmalarmDao;

    @Autowired
    private ScStudentsDao scStudentsDao;

    @Autowired
    private ScWorkersarchivesDao scWorkersarchivesDao;

    @Autowired
    private ScDormitorypersonDao scDormitorypersonDao;

    @Autowired
    private ScFaceverificationDao scFaceverificationDao;

    @Autowired
    private ScVisitorrecordDao scVisitorrecordDao;

    @Autowired
    private AccessRecordsDao accessRecordsDao;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ScDormitoryDao scDormitoryDao;

    @Autowired
    private ScStudentsOutHistoryDao scStudentsOutHistoryDao;

    @Autowired
    private ScAidooreightDao scAidooreightDao;

    @Autowired
    private SysAdminFeignClient sysAdminFeignClient;

    @Autowired
    private ScRoomcurrentEverydayDao scRoomcurrentEverydayDao;

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private ScModbusdevicedcDao scModbusdevicedcDao;

    @Autowired
    private ScTransformerdcDao scTransformerdcDao;

    /**电流互感器设备和宿舍信息*/
    @Autowired
    private ScTransformerroomDao scTransformerroomDao;

    @Autowired
    private  ScCurrenthistoryDao scCurrenthistoryDao;

    @Autowired
    private ScGatewaydcDao scGatewaydcDao;

    @Autowired
    private ScImportantDeviceDao scImportantDeviceDao;

    @Autowired
    private ScCurrenthistorySumDao scCurrenthistorySumDao;

    @Autowired
    private ScThermalImagingDao scThermalImagingDao;

    private LocalDateTime lastSendJsonTime = LocalDateTime.now();

    List<ScGatewaydcEntity> gatewaydcEntities=null;

    private  Map<String, ScModbusdevicedcEntity > mapScModBusdevice=new HashMap<>();
    SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 报警数据
     */
    private Map<String, Object> alarm() {
        Map<String, Object> map = new HashMap<>();
        //电流互感器房间电流信息报警
        QueryWrapper<ScTransformalarmEntity> queryWrapper = new QueryWrapper();
        queryWrapper.orderByDesc("tfa_alarmtime");
        queryWrapper.last("limit  3");
        List<ScTransformalarmEntity> scTransformalarmEntities = scTransformalarmDao.selectList(queryWrapper);

        //uwb报警内容
        QueryWrapper<ScUwbalarmEntity> uwbalarmEntityQueryWrapper = new QueryWrapper();
        uwbalarmEntityQueryWrapper.orderByDesc("uba_alarmtime");
        uwbalarmEntityQueryWrapper.last("limit  3");
        List<ScUwbalarmEntity> scUwbalarmEntities = scUwbalarmDao.selectList(uwbalarmEntityQueryWrapper);

        //PM2.5设备报警信息
        QueryWrapper<ScPmalarmEntity> pmalarmEntityQueryWrapper = new QueryWrapper();
        pmalarmEntityQueryWrapper.orderByDesc("pma_alarmtime");
        pmalarmEntityQueryWrapper.last("limit  3");
        List<ScPmalarmEntity> scPmalarmEntities = scPmalarmDao.selectList(pmalarmEntityQueryWrapper);

        map.put("transformalarm", scTransformalarmEntities);
        map.put("uwbalarm", scUwbalarmEntities);
        map.put("pmalarm", scPmalarmEntities);

        return map;
    }

    private Integer studentsNum() {
        QueryWrapper<ScStudentsEntity> queryWrapper = new QueryWrapper<>();
        return scStudentsDao.selectCount(queryWrapper);
    }

    private Integer workersarchivesNum() {
        QueryWrapper<ScWorkersarchivesEntity> queryWrapper = new QueryWrapper<>();
        return scWorkersarchivesDao.selectCount(queryWrapper);
    }

    /**
     * 学生外出人数与归寝
     */
    private Map<String, Object> dormitory() {
        Map<String, Object> map = new HashMap<>();
        //外出
        QueryWrapper<ScDormitorypersonEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_out", 1);
        Integer dormitoryOutNum = scDormitorypersonDao.selectCount(queryWrapper);
        //归寝
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_out", 0);
        Integer dormitoryBackNum = scDormitorypersonDao.selectCount(queryWrapper);
        //男
        Integer dormitoryManNum = scDormitorypersonDao.countBySex(SexTypeEnum.BOY.code);
        //女
        Integer dormitoryWomanNum = scDormitorypersonDao.countBySex(SexTypeEnum.GIRL.code);
        //未住满
        QueryWrapper<ScDormitoryEntity> queryWrappe = new QueryWrapper<>();
        queryWrappe.eq("df_isfull", 0);
        queryWrappe.eq("dr_state", 2);
        Integer dormitoryNotFull = scDormitoryDao.selectCount(queryWrappe);
        //已住满
        queryWrappe = new QueryWrapper<>();
        queryWrappe.eq("df_isfull", 1).eq("dr_state", 2);
        Integer dormitoryFull = scDormitoryDao.selectCount(queryWrappe);
        //空房间
        queryWrappe = new QueryWrapper<>();
        queryWrappe.eq("dr_state",1);
        Integer dormitoryEmpty =  scDormitoryDao.selectCount(queryWrappe);
        map.put("dormitoryOutNum",dormitoryOutNum);
        map.put("dormitoryBackNum",dormitoryBackNum);
        map.put("dormitoryManNum",dormitoryManNum);
        map.put("dormitoryWomanNum",dormitoryWomanNum);
        map.put("dormitoryNotFull",dormitoryNotFull);
        map.put("dormitoryFull",dormitoryFull);
        map.put("dormitoryEmpty",dormitoryEmpty);
        return map;
    }

    /**
     * 学生周未归统计
     */
    private Map<String, Object> dormitoryWeek() {
        List<Map<String, Object>> dormitoryWeek = new ArrayList<>();
        SimpleDateFormat formatToday = new SimpleDateFormat("yyyy-MM-dd");
        //一周未归人次统计
        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_WEEK, -i);
            String date = formatToday.format(calendar.getTime());
            QueryWrapper<AccessRecordsEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 1).like("out_time", date);
            Integer dormitoryOutNum = accessRecordsDao.selectCount(queryWrapper);
            //日期与未归人次
            Map<String, Object> mp = new HashMap<>();
            mp.put("date", date);
            mp.put("dormitoryOutNum", dormitoryOutNum);
            dormitoryWeek.add(mp);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("dormitoryWeek", dormitoryWeek);
        return map;
    }

    @Override
    public Map<String, Object> dataInfo() {
        //报警数据
        Map<String, Object> map = alarm();
        //学生总人数
        Integer studentsNum = studentsNum();
        map.put("studentsNum", studentsNum);
        //教职工总人数
        Integer workersarchivesNum = workersarchivesNum();
        map.put("workersarchivesNum", workersarchivesNum);
        //宿舍外出归寝人数
        Map<String, Object> dormitory = dormitory();
        map.putAll(dormitory);
        //学生周未归统计
        Map<String, Object> dormitoryWeek = dormitoryWeek();
        map.putAll(dormitoryWeek);
        return map;
    }

    @Override
    public Map<String, Object> aidooreight() {
        Integer entrantsCount = scFaceverificationDao.selectDayCount(new Date(),1);
        Integer outCount = scFaceverificationDao.selectDayCount(new Date(),2);
        Integer retention = entrantsCount - outCount;
        Map<String, Object> map = new HashMap<>();
        map.put("entrantsCount",entrantsCount);
        map.put("outCount",outCount);
        map.put("retention",retention);
        return map;
    }

    @Override
    public Map<String, Object> visitorrecordStatistics() {
        Integer entrantsCount = scVisitorrecordDao.selectDayCount(new Date(),0);
        Integer outCount = scVisitorrecordDao.selectDayCount(new Date(),1);
        Integer retention = entrantsCount - outCount;
        Map<String, Object> map = new HashMap<>();
        map.put("entrantsCount",entrantsCount);
        map.put("outCount",outCount);
        map.put("retention",retention);
        return map;

    }

    @Override
    public Map<String, Object> layeredStatistics() {
        Object uwblayeredStatistics = redisUtils.get("uwblayeredStatistics");
        Map<String, Object> map = new HashMap<>();
        map.put("f1",60);
        map.put("f2",55);
        map.put("f3",20);
        map.put("f4",33);
        map.put("f5",70);
        return map;

    }

    @Override
    public Map<String, Object> dormitoryCheckInStatistics() {
        return dormitory();
    }

    @Override
    public List<ScStudentsOutHistoryDTO> notReturnedStatistics() {
        List<ScStudentsOutHistoryEntity> entities = scStudentsOutHistoryDao.notReturnedStatistics();
        List<ScStudentsOutHistoryDTO> scStudentsOutHistoryDaos = ConvertUtils.sourceToTarget(entities, ScStudentsOutHistoryDTO.class);
        return scStudentsOutHistoryDaos;
    }

    @Override
    public List<ScFaceverificationDTO> dormitoryPeerRecord() {
        List<Long> ids = new ArrayList<>();
        List<ScAidooreightEntity> scAidooreightEntities = scAidooreightDao.selectList(new QueryWrapper<ScAidooreightEntity>().eq("ae_devicetype", 3));
        for (ScAidooreightEntity scAidooreightEntity : scAidooreightEntities) {
            ids.add(scAidooreightEntity.getAeId());
        }
        List<ScFaceverificationDTO> scFaceverificationDTOS =  new ArrayList<>();
        if(ids.size() > 0){
            String format = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
            QueryWrapper<ScFaceverificationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("equipment_id",ids);
            queryWrapper.eq("date(create_date)",format);
//            queryWrapper.orderByDesc("create_date");
            scFaceverificationDTOS = ConvertUtils.sourceToTarget(scFaceverificationDao.selectList(queryWrapper), ScFaceverificationDTO.class);
        }
      return scFaceverificationDTOS;
    }

    @Override
    public List<Map<String, Object>> visitorInformation() {

        //今日日期
        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String today = format.format(new Date());*/

        /*访客人员*/
        //获取今日访客人员
        List<Map<String, Object>> visitorData = scVisitorrecordDao.getVisitorData(null);

        return visitorData;
    }

    @Override
    public List<Map<String, Object>> temperatureWarning() {

        /*温度预警*/
        //从字典码表当中获取高温阈值
        Result temperatureResult = sysAdminFeignClient.getByType("temperature");
        List<LinkedHashMap> temperature = (List<LinkedHashMap>) temperatureResult.getData();
        //将温度阈值保留小数点后两位
        BigDecimal thresholdByTemperature = new BigDecimal(Double.parseDouble(temperature.get(0).get("dictValue").toString())).setScale(2, RoundingMode.UP);
        //获取学生和工作人员的今日访客数据
        List<Map<String, Object>> visitorWarningData = scVisitorrecordDao.getTemperatureWarning(new Date(),thresholdByTemperature);

        return visitorWarningData;
    }

    @Override
    public List<ScCurrenthistorySumEntity> getByTop() {
        List<ScCurrenthistorySumEntity> entity =  scCurrenthistorySumDao.getByTop(sdf.format(new Date()));
        return   entity;
    }

    @Override
    public List<Map<String, Object>> thermalTop() {

        QueryWrapper<ScThermalImagingEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("tf_devicetype", "4","5");
        List<ScThermalImagingEntity> entities = scThermalImagingDao.selectList(queryWrapper);
        List<Map<String, Object>> list =  new ArrayList<>();
        for (ScThermalImagingEntity entity : entities) {
            Map<String, Object> map = new HashMap<>();
            Object o = redisUtils.get("hotmapvalue:" + entity.getTfIpgateway());
            map.put("name",entity.getTfDevicename());
            map.put("ip",entity.getTfIpgateway());
            map.put("temperature",o);
            map.put("date", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
            list.add(map);
        }

        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String name1 = o1.get("temperature") != null ? o1.get("temperature").toString() : "";//name1是从你list里面拿出来的一个
                String name2 = o2.get("temperature") != null ? o2.get("temperature").toString() : "";//name1是从你list里面拿出来的第二个name
                return name2.compareTo(name1);
            }
        });

        return list;

    }



    @Override
    public List<ScFaceverificationDTO> twoBuildingPeerRecord() {
      /*  List<Long> ids = new ArrayList<>();
        List<ScAidooreightEntity> scAidooreightEntities = scAidooreightDao.selectList(new QueryWrapper<ScAidooreightEntity>().in("ae_devicetype", 1,2));
        for (ScAidooreightEntity scAidooreightEntity : scAidooreightEntities) {
            ids.add(scAidooreightEntity.getAeId());
        }
        List<ScFaceverificationDTO> scFaceverificationDTOS =  new ArrayList<>();
        if(ids.size() > 0){
            String format = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
            QueryWrapper<ScFaceverificationEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("equipment_id",ids);
            queryWrapper.eq("date(create_date)",format);
            List<ScFaceverificationEntity> scFaceverificationEntities = scFaceverificationDao.selectList(queryWrapper);
            scFaceverificationDTOS = ConvertUtils.sourceToTarget(scFaceverificationEntities, ScFaceverificationDTO.class);
        }*/
        List<ScFaceverificationDTO> scFaceverificationDTOS = scFaceverificationDao.getLimit();
        return scFaceverificationDTOS;
    }

    @Override
    public Map<String, Object> uwbLabelType() {

        Map<String,Object> map = new HashMap<>();
        //学生
        List<ScStudentsEntity> studentsEntities = scStudentsDao.selectList(new QueryWrapper<ScStudentsEntity>().isNotNull("sc_uwbid"));
        List<String> studentUwb = new ArrayList<>();
        for (ScStudentsEntity studentsEntity : studentsEntities) {
            if(StringUtils.isNoneBlank(studentsEntity.getScUwbid())){
                studentUwb.add(studentsEntity.getScUwbid());
            }
        }
        //教师

        List<String> teacherUwb = new ArrayList<>();
        //保洁
        List<String> cleaningUwb = new ArrayList<>();
        //安保
        List<String> securityUwb = new ArrayList<>();
        List<ScWorkersarchivesEntity> scWorkersarchivesEntities = scWorkersarchivesDao.selectList(new QueryWrapper<ScWorkersarchivesEntity>().isNotNull("sc_uwbid"));
        for (ScWorkersarchivesEntity scWorkersarchivesEntity : scWorkersarchivesEntities) {
            String scUwbid = scWorkersarchivesEntity.getScUwbid();
            if(StringUtils.isNoneBlank(scUwbid)){
                //职工类型：0 教师 1 保洁 2 保安
                Integer scEmptype = scWorkersarchivesEntity.getScEmptype();
                switch (scEmptype){
                    case 0:
                        teacherUwb.add(scUwbid);
                        break;
                    case 1:
                        cleaningUwb.add(scUwbid);
                        break;
                    case 2:
                        securityUwb.add(scUwbid);
                        break;
                }
            }
        }

        //访客
        List<String> visitorUwb = new ArrayList<>();
        List<ScVisitorrecordEntity> scVisitorrecordEntities = scVisitorrecordDao.selectList(new QueryWrapper<ScVisitorrecordEntity>().isNotNull("vr_uwbid").eq("vr_returnuwb", 0));
        for (ScVisitorrecordEntity scVisitorrecordEntity : scVisitorrecordEntities) {
            if(StringUtils.isNoneBlank(scVisitorrecordEntity.getVrUwbid())){
                visitorUwb.add(scVisitorrecordEntity.getVrUwbid());
            }
        }
        //重控设施
        List<String> importantUwb = new ArrayList<>();
        List<ScImportantDeviceEntity> scImportantDeviceEntities = scImportantDeviceDao.selectList(new QueryWrapper<ScImportantDeviceEntity>().isNotNull("uwb"));
        for (ScImportantDeviceEntity scImportantDeviceEntity : scImportantDeviceEntities) {
            if(StringUtils.isNoneBlank(scImportantDeviceEntity.getUwb())){
                importantUwb.add(scImportantDeviceEntity.getUwb());
            }
        }

        map.put("studentUwb",studentUwb);
        map.put("teacherUwb",teacherUwb);
        map.put("cleaningUwb",cleaningUwb);
        map.put("securityUwb",securityUwb);
        map.put("visitorUwb",visitorUwb);
        map.put("importantUwb",importantUwb);

        return map;
    }

    @Override
    public List<ScRoomcurrentEverydayDTO> roomCurrentAlarm() {
        List<ScRoomcurrentEverydayDTO> scRoomcurrentEverydayDTOS;
        Object roomCurrentAlarm = redisUtils.get("roomCurrentAlarm");
        if(roomCurrentAlarm != null){
            scRoomcurrentEverydayDTOS = JSONArray.parseArray(roomCurrentAlarm.toString(), ScRoomcurrentEverydayDTO.class);
        }else{
            List<ScRoomcurrentEverydayEntity> entities = scRoomcurrentEverydayDao.roomCurrentAlarm();
            scRoomcurrentEverydayDTOS =  ConvertUtils.sourceToTarget(entities,ScRoomcurrentEverydayDTO.class);
            redisUtils.set("roomCurrentAlarm",JSONObject.toJSONString(scRoomcurrentEverydayDTOS),5 * 60L);
        }
        return scRoomcurrentEverydayDTOS;
    }

   @RabbitListener(queues = MQQueueNameConfig.MQ_HOME_SCHOOL_ACK_ALARM_QUEUE)
    public void mqGet(String alarmmsg, Channel channel, Message message) throws IOException {
        log.info("环境传感器&电流&管道MQ数据: " + alarmmsg);
        try {
            MqAlarmMsgDTO mqAlarmMsgDTO = JSONObject.parseObject(alarmmsg, MqAlarmMsgDTO.class);
           /* if(mqAlarmMsgDTO!=null&&mqAlarmMsgDTO.getAlarmtype()== AlarmTypeEnum.ENV_ALARM){
                //环境温度报警
                EnvironmentAlarmDTO environmentAlarmDTO = JSONObject.parseObject(mqAlarmMsgDTO.getAlarmObject().toString(), EnvironmentAlarmDTO.class);
                //设备ID
                String mbdid = environmentAlarmDTO.getMbdid();
                ScModbusdevicedcEntity scModbusdevicedcEntity = scModbusdevicedcDao.selectById(mbdid);
                if(scModbusdevicedcEntity != null){
                    JSONObject data = new JSONObject();
                    data.put("floorAddr",scModbusdevicedcEntity.getMbdUwbaddr());
                    data.put("pm",environmentAlarmDTO.getPm());
                    data.put("temperature",environmentAlarmDTO.getTemperature());
                    data.put("humidity",environmentAlarmDTO.getHumidity());
                    data.put("devicename",scModbusdevicedcEntity.getMbdDevicename());
                    data.put("coordinate",scModbusdevicedcEntity.getMbdCoordinate());
                    data.put("deviceId",scModbusdevicedcEntity.getMbdId());
                    MessageData messageData = new MessageData();
                    messageData.setType(1);
                    messageData.setData(data);
                    messageData.setMsg("环境传感器报警数据");
                    webSocketServer.sendMessageAll(messageData);
                }

            }else*/ if(mqAlarmMsgDTO!=null&&mqAlarmMsgDTO.getAlarmtype()== AlarmTypeEnum.ELECTRIC_ALARM){
                // 电流报警
                RoadEctricCurrent ectricCurrent = JSONObject.parseObject(mqAlarmMsgDTO.getAlarmObject().toString(), RoadEctricCurrent.class);
                handlerEectricCurrent(ectricCurrent);
            }else if (mqAlarmMsgDTO!=null&&mqAlarmMsgDTO.getAlarmtype()== AlarmTypeEnum.PIPELINE_ALARM){
                //管道报警
                ScThermalAlarmEntity scThermalAlarmEntity = JSONObject.parseObject(mqAlarmMsgDTO.getAlarmObject().toString(), ScThermalAlarmEntity.class);
                //字段名称与电流报警保持一致
                JSONObject jsonObject = new JSONObject();
                //查询热成像信息
                ScThermalImagingEntity scThermalImagingEntity = scThermalImagingDao.selectById(scThermalAlarmEntity.getTfId());
                if(scThermalImagingEntity != null){
                    //报警值
                    jsonObject.put("tfaEcurrent",scThermalAlarmEntity.getTfaEcurrent());
                    //报警时间
                    jsonObject.put("tfaAlarmtime",DateUtils.format(scThermalAlarmEntity.getTfaAlarmtime(),DateUtils.DATE_TIME_PATTERN));
                    //报警位置
                    jsonObject.put("drNum",scThermalImagingEntity.getTfDevicename() );
                    jsonObject.put("isAlarm",scThermalAlarmEntity.isNeedAlarm());
                    jsonObject.put("IP",scThermalImagingEntity.getTfIpgateway());
                    jsonObject.put("tfDevicetype",scThermalImagingEntity.getTfDevicetype());
                    MessageData messageData = new MessageData();
                    messageData.setType(4);
                    messageData.setData(jsonObject);
                    messageData.setMsg("管道报警数据");
                    webSocketServer.sendMessageAll(messageData);
                }
            }

            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false); //手工确认消息
        } catch (Exception e) {
            e.printStackTrace();
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            // log.error("MQ_ACK_WARNING_QUEUE 接受信息异常{}", e.getMessage());
        }

    }

    private  void handlerEectricCurrent(RoadEctricCurrent ectricCurrent){

        /**根据设备ip地址查询对应的设备编号信息*/
        ScTransformerdcEntity ipgateway = scTransformerdcDao.selectOne(new QueryWrapper<ScTransformerdcEntity>().eq("tf_ipgateway", ectricCurrent.getIp()));
        if(ipgateway != null){
            /**根据设备编号查询  电流互感器设备和宿舍信息**/
            QueryWrapper<ScTransformerroomEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tf_id", ipgateway.getTfId());
            queryWrapper.eq("tfr_portaddr", ectricCurrent.getRoad());
            ScTransformerroomEntity scTransformerroomEntity = scTransformerroomDao.selectOne(queryWrapper);
            if (scTransformerroomEntity != null) {
                JSONObject jsonObject = new JSONObject();
                //设备ID
                jsonObject.put("tfId",ipgateway.getTfId());
                //设备编号
                jsonObject.put("tfDevicesn",StringUtils.isNotEmpty(ipgateway.getTfDevicesn()) ? ipgateway.getTfDevicesn() : "");
                //电流值
                jsonObject.put("tfaEcurrent",ectricCurrent.getElectricValue());
                //报警时间
                jsonObject.put("tfaAlarmtime",DateUtils.format(ectricCurrent.getCollecttime(),DateUtils.DATE_TIME_PATTERN));
                //房间编号
                String roomNum = StringUtils.isNotEmpty(scTransformerroomEntity.getTfrDrroomno()) ? scTransformerroomEntity.getTfrDrroomno() : "";
                jsonObject.put("drNum",roomNum);
                //房间ID
                Long roomID = scTransformerroomEntity.getDrId();
                jsonObject.put("drRoomid",roomID);
                //是否报警
                jsonObject.put("isAlarm",ectricCurrent.isNeedAlarm());

                MessageData messageData = new MessageData();
                messageData.setType(2);
                messageData.setData(jsonObject);
                messageData.setMsg("电流报警数据");
                webSocketServer.sendMessageAll(messageData);
            }else{
                log.error("房间不存在在 : " + ectricCurrent.getRoad());
            }
        }else {
            log.error("设备不存在在 : " + ectricCurrent.getIp());
        }

    }


    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    private void PmDataInfo(){
        Set<String> keys = redisUtils.keys("PM*");
        if(keys != null&&keys.size()>0) {

            java.time.Duration duration = java.time.Duration.between(lastSendJsonTime, LocalDateTime.now());
            if (duration.toMillis() >20 || gatewaydcEntities==null||gatewaydcEntities.size()==0) {
                 gatewaydcEntities = scGatewaydcDao.selectList(new QueryWrapper<ScGatewaydcEntity>().eq("mbd_devicetype", 2));
                 mapScModBusdevice.clear();
                 lastSendJsonTime = LocalDateTime.now();
            }
            List<JSONObject> dataList = new ArrayList<>();

            for (ScGatewaydcEntity gatewaydcEntity : gatewaydcEntities) {
//        JSONObject jsonObject = JSONObject.parseObject("{\"1316739066653388802\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739066653388802\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739067811016705\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739067811016705\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739068050092034\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739068050092034\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739069555847169\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739069555847169\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739069136416770\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739069136416770\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739066921824258\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739066921824258\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739067144122370\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739067144122370\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739067580329986\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739067580329986\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739068926701569\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739068926701569\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739069765562370\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739069765562370\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739068289167362\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739068289167362\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739068507271169\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739068507271169\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739069341937666\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739069341937666\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739068721180673\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739068721180673\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739067383197697\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739067383197697\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"},\"1316739069983666178\":{\"humidity\":\"0\",\"isOffdevice\":\"false\",\"mbdid\":\"1316739069983666178\",\"pm\":\"0\",\"temperature\":\"0\",\"timestamp\":\"\"}}");
                Object o = redisUtils.get("PM" + gatewaydcEntity.getGwId().toString());
                if (o != null) {
                    JSONObject jsonObject = JSONObject.parseObject(o.toString());
                    for (String s : jsonObject.keySet()) {
                        PMRedisNode pmRedisNode = JSONObject.parseObject(jsonObject.getString(s), PMRedisNode.class);
                        //设备ID
                        String mbdid = pmRedisNode.getMbdid();

                        ScModbusdevicedcEntity scModbusdevicedcEntity = mapScModBusdevice.get(mbdid);
                        if(scModbusdevicedcEntity==null){
                             scModbusdevicedcEntity = scModbusdevicedcDao.selectById(mbdid);
                            mapScModBusdevice.put(mbdid,scModbusdevicedcEntity);
                        }
                        if (scModbusdevicedcEntity != null) {
                            JSONObject data = new JSONObject();
                            data.put("floorAddr", scModbusdevicedcEntity.getMbdUwbaddr());
                            data.put("pm", pmRedisNode.getPm());
                            data.put("temperature", pmRedisNode.getTemperature());
                            data.put("humidity", pmRedisNode.getHumidity());
                            data.put("devicename", scModbusdevicedcEntity.getMbdDevicename());
                            data.put("coordinate", scModbusdevicedcEntity.getMbdCoordinate());
                            data.put("isSendAlarm", pmRedisNode.getIsSendAlarm());
                            data.put("isOffdevice", pmRedisNode.getIsOffdevice());
                            data.put("deviceId", scModbusdevicedcEntity.getMbdId());
                            data.put("ispmAlarm", pmRedisNode.getIspmAlarm());
                            data.put("istemperatureAlarm", pmRedisNode.getIstemperatureAlarm());
                            data.put("ishumidityAlarm", pmRedisNode.getIshumidityAlarm());
                            data.put("mbdDevicesn", scModbusdevicedcEntity.getMbdDevicesn());
                            dataList.add(data);
//                        MessageData messageData = new MessageData();
//                        messageData.setType(1);
//                        messageData.setData(data);
//                        messageData.setMsg("环境传感器报警数据");
//                        webSocketServer.sendMessageAll(messageData);
                        }

                    }
                }
            }
            if(dataList.size() > 0){
                MessageData messageData = new MessageData();
                messageData.setType(1);
                messageData.setData(dataList);
                messageData.setMsg("环境传感器报警数据");
                webSocketServer.sendMessageAll(messageData);
            }
        }

    }


}
