package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScCurrenthistoryDTO;
import com.dkha.entity.ScCurrenthistoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScCurrenthistoryDao extends BaseDao<ScCurrenthistoryEntity> {

    List<ScCurrenthistoryEntity> getByRoomId(@Param("roomId") Long roomId, @Param("startTime") String startTime, @Param("endTime") String endTime);
    /**查询前十电流值*/
    List<ScCurrenthistoryEntity> getByTop();

}
