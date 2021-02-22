package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScAttendanceStatisticsDataDTO;
import com.dkha.entity.ScAttendanceStatisticsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 考勤统计
 *
 * @author Mark 
 * @since v1.0.0 2020-12-14
 */
@Mapper
public interface ScAttendanceStatisticsDao extends BaseDao<ScAttendanceStatisticsEntity> {

    List<ScAttendanceStatisticsDataDTO> dataInfo(@Param("params") Map<String, Object> params);

    List<ScAttendanceStatisticsEntity> list(@Param("params") Map<String, Object> params);
}