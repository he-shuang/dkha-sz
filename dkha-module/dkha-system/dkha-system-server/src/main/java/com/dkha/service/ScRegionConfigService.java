package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScRegionConfigDTO;
import com.dkha.dto.UwbRegionDTO;
import com.dkha.entity.ScRegionConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 区域配置/uwb围栏关联
 *
 * @author Mark
 * @since v1.0.0 2020-09-01
 */
public interface ScRegionConfigService extends BaseService<ScRegionConfigEntity> {

    PageData<ScRegionConfigDTO> page(Map<String, Object> params);

    List<ScRegionConfigDTO> list(Map<String, Object> params);

    ScRegionConfigDTO get(String id);

    void save(ScRegionConfigDTO dto);

    void update(ScRegionConfigDTO dto);

    void delete(String[] ids);

    List<UwbRegionDTO> getUwbRegionList();
}
