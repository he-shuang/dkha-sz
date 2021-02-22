package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScConsumptionsystemConsumeDTO;
import com.dkha.entity.ScConsumptionsystemConsumeEntity;

import java.util.List;
import java.util.Map;

/**
 * 消费系统的消费记录
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
public interface ScConsumptionsystemConsumeService extends BaseService<ScConsumptionsystemConsumeEntity> {

    PageData<ScConsumptionsystemConsumeDTO> page(Map<String, Object> params);

    List<ScConsumptionsystemConsumeDTO> list(Map<String, Object> params);

    ScConsumptionsystemConsumeDTO get(String id);

    void save(ScConsumptionsystemConsumeDTO dto);

    void update(ScConsumptionsystemConsumeDTO dto);

    void delete(String[] ids);
}