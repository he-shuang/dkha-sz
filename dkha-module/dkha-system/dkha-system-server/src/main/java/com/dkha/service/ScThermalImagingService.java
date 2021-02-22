package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScThermalImagingDTO;
import com.dkha.entity.ScThermalImagingEntity;

import java.util.List;
import java.util.Map;

/**
 * 热成像设备表
 *
 * @author Mark
 * @since v1.0.0 2020-11-04
 */
public interface ScThermalImagingService extends BaseService<ScThermalImagingEntity> {

    PageData<ScThermalImagingDTO> page(Map<String, Object> params);

    List<ScThermalImagingDTO> list(Map<String, Object> params);

    ScThermalImagingDTO get(String id);

    void save(ScThermalImagingDTO dto);

    void update(ScThermalImagingDTO dto);

    void delete(String ids);

    List<Map<String, Object>> thermalList(String type,Map<String, Object> params);
}
