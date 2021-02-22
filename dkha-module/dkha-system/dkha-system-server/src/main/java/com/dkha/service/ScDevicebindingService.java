package com.dkha.service;


import com.dkha.commons.mybatis.service.CrudService;
import com.dkha.dto.AlarmMassge;
import com.dkha.dto.ScDevicebindingDTO;
import com.dkha.entity.ScDevicebindingEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 星网云联设备位置绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
public interface ScDevicebindingService extends CrudService<ScDevicebindingEntity, ScDevicebindingDTO> {
    /**
     *获取所有的设备信息
     */
    List<ScDevicebindingEntity> getBadingDevice();

    /**
     *
     * @return
     */
    List<ScDevicebindingEntity>  getAllDevice();

    /**
     *未绑定的设备
     * @return
     */
    List<ScDevicebindingEntity> getNoBangding();

    /**
     * 报警设备
     */
    List<AlarmMassge>  getIsAlarm();

    /**
     * 报警历史设备
     * @return
     */
    List<AlarmMassge> getAlarmHistory(String startTime,String endTime);

    /**
     * 报警状态处理
     * @param alarmMassge
     */
    void handleAlarm( AlarmMassge alarmMassge);
}