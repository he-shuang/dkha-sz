package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScAttendanceStatisticsDTO;
import com.dkha.dto.ScAttendanceStatisticsDataDTO;
import com.dkha.entity.ScAttendanceStatisticsEntity;

import java.util.List;
import java.util.Map;

/**
 * 考勤统计
 *
 * @author Mark 
 * @since v1.0.0 2020-12-14
 */
public interface ScAttendanceStatisticsService extends BaseService<ScAttendanceStatisticsEntity> {

    PageData<ScAttendanceStatisticsDTO> page(Map<String, Object> params);

    List<ScAttendanceStatisticsDTO> list(Map<String, Object> params);

    ScAttendanceStatisticsDTO get(String id);

    void save(ScAttendanceStatisticsDTO dto);

    void update(ScAttendanceStatisticsDTO dto);

    void delete(String[] ids);

    List<ScAttendanceStatisticsDataDTO> dataInfo(Map<String, Object> params);
}