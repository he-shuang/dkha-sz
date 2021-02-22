package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScRoomcurrentEverydayEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 宿舍电流每日报警记录
 *
 * @author Mark
 * @since v1.0.0 2020-10-10
 */
@Mapper
public interface ScRoomcurrentEverydayDao extends BaseDao<ScRoomcurrentEverydayEntity> {

    List<ScRoomcurrentEverydayEntity> roomCurrentAlarm();
}
