package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScGateaccesscontrolDTO;
import com.dkha.entity.ScGateaccesscontrolEntity;


import java.util.List;
import java.util.Map;

/**
 * 门禁同行记录
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScGateaccesscontrolService extends BaseService<ScGateaccesscontrolEntity> {

    PageData<ScGateaccesscontrolDTO> page(Map<String, Object> params);

    List<ScGateaccesscontrolDTO> list(Map<String, Object> params);

    ScGateaccesscontrolDTO get(String id);

    void save(ScGateaccesscontrolDTO dto);

    void update(ScGateaccesscontrolDTO dto);

    void delete(String[] ids);
}
