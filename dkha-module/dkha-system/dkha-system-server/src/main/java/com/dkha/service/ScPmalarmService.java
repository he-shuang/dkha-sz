package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScPmalarmDTO;
import com.dkha.entity.ScPmalarmEntity;


import java.util.List;
import java.util.Map;

/**
 * PM2.5设备报警信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScPmalarmService extends BaseService<ScPmalarmEntity> {

    PageData<ScPmalarmDTO> page(Map<String, Object> params);

    List<ScPmalarmDTO> list(Map<String, Object> params);

    ScPmalarmDTO get(String id);

    void save(ScPmalarmDTO dto);

    void update(ScPmalarmDTO dto);

    void delete(String[] ids);
}
