package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScUwbLabelToInfoDTO;
import com.dkha.dto.VisitorStatisticsDTO;
import com.dkha.entity.ScVisitorrecordEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 访客记录表
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScVisitorrecordDao extends BaseDao<ScVisitorrecordEntity> {
    /**
     * 获取分页数据
     * @param params
     * @return
     */
    List<ScVisitorrecordEntity> getMyList(Map<String, Object> params);

    /**
     * 获取分页总数
     * @param params
     * @return
     */
    Long getMyCount(Map<String, Object> params);

    /**
     * 访客还卡
     * @param vrId 访客记录ID
     * @param userId 操作人ID
     */
    void returnCard(@Param("vrId") String vrId, @Param("userId") Long userId);

    /**
     * 获取同一楼层未还卡uwbId
     * @param dfFloorid 楼层ID
     */
    List<Long> getListUwbId(@Param("dfFloorid") Long dfFloorid);

    /**
     * 访客记录历史轨迹访客信息
     * @param vrId 访客记录ID
     */
    ScUwbLabelToInfoDTO getMyScUwbLabelToInfo(@Param("vrId") String vrId);

    Integer selectDayCount(@Param("date") Date date, @Param("type") int type);

    /**
     * 今日访客信息
     * @param today
     * @return
     */
    List<Map<String, Object>> getVisitorData(@Param("today") String today);

    /**
     * 高温预警信息
     * @param today
     * @param temperature
     * @return
     */
    List<Map<String,Object>> getTemperatureWarning(@Param("today") Date today,@Param("temperature") BigDecimal temperature);

    List<ScVisitorrecordEntity> list(Map<String, Object> params);

    List<VisitorStatisticsDTO> visitorWeek(@Param("params") Map<String, Object> params);

    List<VisitorStatisticsDTO> visitorMonth(@Param("params") Map<String, Object> params);

    List<VisitorStatisticsDTO> visitorWorkersWeek(@Param("params")Map<String, Object> params);

    List<VisitorStatisticsDTO> visitorWorkersMonth(@Param("params")Map<String, Object> params);
}
