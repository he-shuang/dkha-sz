package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScStatisticsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author dkha 
 * @since v1.0.0 2020-10-27
 */
@Mapper
public interface ScStatisticsDao extends BaseDao<ScStatisticsEntity> {

    List<ScStatisticsEntity> getByCountWeek(@Param("params") Map<String, Object> params);

    List<ScStatisticsEntity> getByCountMonths(@Param("params")Map<String, Object> params);

    List<ScStatisticsEntity> getByAcademicWeek(@Param("params") Map<String, Object> params);
    List<ScStatisticsEntity> getByAcademicMonths(@Param("params") Map<String, Object> params);

}