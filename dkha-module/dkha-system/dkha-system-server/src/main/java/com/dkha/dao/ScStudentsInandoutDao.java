package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.DataAnalysisDTO;
import com.dkha.dto.ScRoomCountDTO;
import com.dkha.entity.ScStudentsInandoutEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author dkha 
 * @since v1.0.0 2020-10-22
 */
@Mapper
public interface ScStudentsInandoutDao extends BaseDao<ScStudentsInandoutEntity> {



    /** 宿舍楼统计
     异常类型按周统计 */
    List<ScRoomCountDTO> getByRoomCountWeek(@Param("params") Map<String, Object> params);
    /**异常类型按月统计 */
    List<ScRoomCountDTO> getByRoomCountMoth(@Param("params")Map<String, Object> params);
    /**异常类型按 人 月统计 */
    List<ScRoomCountDTO> getByabnormalCountWeek(@Param("params")Map<String, Object> params);
    /**异常类型按 人 月统计 */
    List<ScRoomCountDTO>  getByabnormalCountMoth(@Param("params")Map<String, Object> params);
    /** 教学楼统计
     异常类型按周统计 */
    List<ScRoomCountDTO> getByAcademicBuildingWeek();
    /**异常类型按月统计 */
    List<ScRoomCountDTO> getByAcademicBuildingMonths();
    /**异常类型按 人 月统计 */
    List<ScRoomCountDTO> getByAcademicBuildingPersonneMonths();
    /**异常类型按人 月统计 */
    List<ScRoomCountDTO> getByAcademicBuildingPersonneWeeks();

    List<DataAnalysisDTO> notInOrOut(@Param("params")Map<String, Object> params);

    List<DataAnalysisDTO> onlyInNotOut(@Param("params") Map<String, Object> params);

    List<DataAnalysisDTO> onlyOutNotIn(@Param("params") Map<String, Object> params);
}