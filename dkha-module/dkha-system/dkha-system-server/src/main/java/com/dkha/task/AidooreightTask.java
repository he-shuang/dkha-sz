package com.dkha.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.tools.utils.SpringContextUtils;
import com.dkha.dao.ScAidooreightDao;
import com.dkha.dto.ScAidooreightDTO;
import com.dkha.service.ScAidooreightService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * All rights 成都电科慧安
 *
 * @ClassName: AidooreightTask
 * @description: 智能设备定时任务处理设备连接+并进行设备的时间同步
 * @author: linhuacheng
 * @create: 2020/9/16 11:02
 **/
@Slf4j
//@Component
//@EnableScheduling
public class AidooreightTask {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private ScAidooreightService scAidooreightService;


    /*
     *  0 0 1 * * ?       每天凌晨1点执行一次
     *  0/5 * * * * ?     每隔5秒执行一次
     *  0 0/30 * * * ?    每隔30分钟执行一次
     *  0 0 0/1 * * ?     每隔1小时执行一次
     *  fixedDelay = 1000 * 60 * 5 间隔5分钟
     * */
//    @Scheduled(fixedDelay = 3600000, initialDelay = 1000 * 15)
    public void configureTasks() {
        String activeProfile = SpringContextUtils.getActiveProfile();
        //生成环境执行,防止设备连接错误
        if("prod".equals(activeProfile)){
            // 执行任务
            logger.error("设备连接定时任务执行开始***************: " + System.currentTimeMillis());
            List<ScAidooreightDTO> dtos = scAidooreightService.listNoStopDevice();
            for (ScAidooreightDTO dto : dtos) {
                boolean b = scAidooreightService.connetDoorConnet(dto.getAeId().toString());
                if(!b){
                    log.error(dto.getAeClientip() + "重新连接失败");
                }else {
                    //链接成功的设备更新设备时间
                    scAidooreightService.updateMachineTimeBy(dto);
                }
            }
            logger.error("设备连接定时任务执行结束***************: " + System.currentTimeMillis());
        }

    }
}
