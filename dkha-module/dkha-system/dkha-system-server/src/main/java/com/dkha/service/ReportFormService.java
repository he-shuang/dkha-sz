package com.dkha.service;

import com.dkha.dto.ScRoomCountDTO;
import com.dkha.dto.VisitorStatisticsDTO;
import com.dkha.entity.ScStatisticsEntity;

import java.util.List;
import java.util.Map;

public interface ReportFormService {
    List<VisitorStatisticsDTO> visitorWeek();

    List<VisitorStatisticsDTO> visitorMonth();

    List<ScRoomCountDTO> roomWeek();
    List<ScRoomCountDTO> getByRoomCountMoth();

    List<ScRoomCountDTO>  getByabnormalCountWeek();
    List<ScRoomCountDTO>  getByabnormalCountMoth();

    /** 教学楼统计
     异常类型按周统计 */
    List<ScRoomCountDTO> getByAcademicBuildingWeek();
    /**异常类型按月统计 */
    List<ScRoomCountDTO> getByAcademicBuildingMonths();
    /**异常类型按 人 月统计 */
    List<ScRoomCountDTO> getByAcademicBuildingPersonneMonths();
    /**异常类型按人 月统计 */
    List<ScRoomCountDTO> getByAcademicBuildingPersonneWeeks();

    List<VisitorStatisticsDTO> visitorWorkersWeek();

    List<VisitorStatisticsDTO> visitorWorkersMonth();

    List<ScStatisticsEntity> roomStatistics(Map<String, Object> params);

    List<VisitorStatisticsDTO> visitorWeekMonth(Map<String, Object> params);

    List<VisitorStatisticsDTO> visitorWorkersWeekMonth(Map<String, Object> params);
    List<ScStatisticsEntity>  academicStatistics(Map<String, Object> params);


    List<ScStatisticsEntity>  getByCountWeek(Map<String, Object> params);
    List<ScStatisticsEntity>  getByCountMonths(Map<String, Object> params);
    List<ScStatisticsEntity>  getByAcademicWeek(Map<String, Object> params);
    List<ScStatisticsEntity>  getByAcademicMonths(Map<String, Object> params);

}
