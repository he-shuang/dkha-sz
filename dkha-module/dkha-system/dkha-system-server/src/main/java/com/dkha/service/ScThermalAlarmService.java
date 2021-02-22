package com.dkha.service;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScThermalAlarmDTO;
import com.dkha.entity.ScThermalAlarmEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 热成像报警表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
public interface ScThermalAlarmService extends BaseService<ScThermalAlarmEntity> {

    PageData<ScThermalAlarmDTO> page(Map<String, Object> params);

    List<ScThermalAlarmDTO> list(Map<String, Object> params);

    ScThermalAlarmDTO get(String id);

    PageData<ScThermalAlarmDTO> getThermalAlarmByPage(@Param(Constants.WRAPPER)Map<String, Object> params);

    void save(ScThermalAlarmDTO dto);

    void update(ScThermalAlarmDTO dto);

    void delete(String[] ids);
}