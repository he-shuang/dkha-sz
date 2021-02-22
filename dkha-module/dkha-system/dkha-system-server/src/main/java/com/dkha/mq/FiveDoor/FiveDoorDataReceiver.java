package com.dkha.mq.FiveDoor;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.rabbitmq.MQQueueNameConfig;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.dao.ScAidoorfivePersonlistDao;
import com.dkha.dao.ScDormitorypersonDao;
import com.dkha.dto.ScAidoorfiveOpenLogDTO;
import com.dkha.entity.FvScDeviceEntity;
import com.dkha.entity.ScAidoorfivePersonlistEntity;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.service.FvScDeviceService;
import com.dkha.service.ScAidoorfiveOpenLogService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * 五寸门禁注册状态队列接收处理
 */
@Component
@Slf4j
public class FiveDoorDataReceiver {

    @Autowired
    private FvScDeviceService fvScDeviceService;
    @Autowired
    private ScAidoorfivePersonlistDao scAidoorfivePersonlistDao;
    @Autowired
    private ScAidoorfiveOpenLogService scAidoorfiveOpenLogService;

    @Autowired
    private ScDormitorypersonDao scDormitorypersonDao;

//    @RabbitListener(queues = MQQueueNameConfig.FIVE_DOOR_REGISTER_QUEUE)
    public void registerMsgGet(String msg, Channel channel, Message message) throws IOException {
        try {

            this.registerSaveDateToDb(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            if (log.isErrorEnabled()) {
                log.info("ACK_QUEUE_A 接受信息异常{}", e.getMessage());
            }
        }
    }

    @RabbitListener(queues = MQQueueNameConfig.FACE_OPEN_LOG_QUEUE)
    public void openLogMsgGet(String msg, Channel channel, Message message) throws IOException {
        try {

            this.openLogsaveDateToDb(msg);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            if (log.isErrorEnabled()) {
                log.info("ACK_QUEUE_A 接受信息异常{}", e.getMessage());
            }
        }
    }

    /**
     * 人脸权限添加
     *
     * @param msg
     */
    private void registerSaveDateToDb(String msg) throws URISyntaxException, InterruptedException {

        JSONObject jsonObject = JSONObject.parseObject(msg);
        String serial = jsonObject.get("serial").toString();
        String userId = jsonObject.get("userId").toString();
        String status = jsonObject.get("status").toString();
        //人脸注册成功并下发权限后返回人脸图片
        String imgId = fvScDeviceService.faceRegister(serial, userId, status);
        //ID判断人脸图片ID是否存在
        if (imgId != null) {
            //根据返回的人脸图片ID查找相应的人脸下发记录
            QueryWrapper<ScAidoorfivePersonlistEntity> fivePersonWrapper = new QueryWrapper<>();
            fivePersonWrapper.eq("img_id", imgId);
            ScAidoorfivePersonlistEntity scAidoorfivePersonlistEntity = scAidoorfivePersonlistDao.selectOne(fivePersonWrapper);
            //更改人脸注册状态
            if (scAidoorfivePersonlistEntity != null) {
                scAidoorfivePersonlistEntity.setStatus(1);
                scAidoorfivePersonlistEntity.setCompleteTime(new Date());
                scAidoorfivePersonlistDao.updateById(scAidoorfivePersonlistEntity);
            }
        }
    }

    /**
     * 五寸门禁系统通行记录添加
     *
     * @param msg
     */
    private void openLogsaveDateToDb(String msg) {
        //从JSON数据当中获取相应参数
        JSONObject jsonObject = JSONObject.parseObject(msg);
        //设备序列号
        String serial = jsonObject.get("serial").toString();
        //人脸十进制ID
        String cardNo = jsonObject.get("cardNo").toString();
        //人脸开门时间戳
        String openTime = jsonObject.get("openTime").toString();

        //从设备库中查询相应的设备
        FvScDeviceEntity fvScDeviceEntity = fvScDeviceService.getBySerial(serial);

        //人脸ID会以逗号隔开的形式传很多相同值，因此转换为数组取第一个
        String[] imgIds = cardNo.split(",");
        //人脸图片ID
        String imgId = leftPading(Integer.toHexString(Integer.parseInt(imgIds[0])), "0", 8).toUpperCase();

        //开门时间戳会以逗号隔开的形式传很多相同值，因此转换为数组取第一个
        String[] openTimes = openTime.split(",");
        //开门时间戳
        openTime = openTimes[0];

        //根据设备序列号、设备密码、相关人脸ID查询人脸下发记录
        QueryWrapper<ScAidoorfivePersonlistEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("serial", fvScDeviceEntity.getFSerial());
        queryWrapper.eq("password", fvScDeviceEntity.getFPassword());
        queryWrapper.eq("img_id", imgId);
        ScAidoorfivePersonlistEntity scAidoorfivePersonlistEntity = scAidoorfivePersonlistDao.selectOne(queryWrapper);

        //判断人脸下发记录当中有无相关数据
        if (scAidoorfivePersonlistEntity != null) {
            //设备名称
            String deviceName = fvScDeviceEntity.getFName();
            //设备序列号
            String serialNumber = fvScDeviceEntity.getFSerialNumber();
            //开门时间
            String addDate = DateUtils.format(DateUtils.secondToDate(Long.valueOf(openTime)), DateUtils.DATE_TIME_PATTERN);
            //姓名
            String name = scAidoorfivePersonlistEntity.getUsername();
            //人脸图片相对路径
            String faceUrl = scAidoorfivePersonlistEntity.getPhotoimg();
            //人员ID
            String userId = scAidoorfivePersonlistEntity.getUserId();

            //开门记录赋值
            ScAidoorfiveOpenLogDTO scAidoorfiveOpenLogDTO = new ScAidoorfiveOpenLogDTO();
            scAidoorfiveOpenLogDTO.setDeviceName(deviceName);
            scAidoorfiveOpenLogDTO.setSerialNumber(serialNumber);
            scAidoorfiveOpenLogDTO.setAddDate(addDate);
            scAidoorfiveOpenLogDTO.setName(name);
            scAidoorfiveOpenLogDTO.setFaceUrl(faceUrl);
            scAidoorfiveOpenLogDTO.setUserId(userId);
            //开门记录存储
            scAidoorfiveOpenLogService.save(scAidoorfiveOpenLogDTO);


            //修改归寝状态
            try {
                boolean matches = deviceName.trim().matches("\\d+");
                if(matches){
                    ScDormitorypersonEntity scDormitorypersonEntity = scDormitorypersonDao.selectOne(new QueryWrapper<ScDormitorypersonEntity>().eq("sc_stdid", userId));
                    if(scDormitorypersonEntity.getIsOut() == 1){
                        scDormitorypersonEntity.setIsOut(0);
                        scDormitorypersonDao.updateById(scDormitorypersonEntity);
                    }
                }
            }catch (Exception e){
                log.error("修改归寝状态异常: ", e);
            }

            // log.error("新添的开门记录:{}",scAidoorfiveOpenLogDTO);
        }
    }

    /**
     * 十进制转换为十六进制并补全8位数
     * @param strSrc
     * @param flag
     * @param strSrcLength
     * @return
     */
    public String leftPading(String strSrc, String flag, int strSrcLength) {
        String strReturn = "";
        String strtemp = "";
        int curLength = strSrc.trim().length();
        if (strSrc != null && curLength > strSrcLength) {
            strReturn = strSrc.trim().substring(0, strSrcLength);
        } else if (strSrc != null && curLength == strSrcLength) {
            strReturn = strSrc.trim();
        } else {

            for (int i = 0; i < (strSrcLength - curLength); i++) {
                strtemp = strtemp + flag;
            }

            strReturn = strtemp + strSrc.trim();
        }
        return strReturn;
    }

}
