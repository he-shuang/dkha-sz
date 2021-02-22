package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.AlarmMassge;
import com.dkha.entity.ScDevicebindingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 星网云联设备位置绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Mapper
public interface ScDevicebindingDao extends BaseDao<ScDevicebindingEntity> {
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
  List<AlarmMassge> getAlarmHistory(@Param("startTime")String startTime, @Param("endTime")String endTime);

}