package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScCurrenthistorySumDTO;
import com.dkha.entity.ScCurrenthistorySumEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-11-06
 */
public interface ScCurrenthistorySumService extends BaseService<ScCurrenthistorySumEntity> {

    PageData<ScCurrenthistorySumDTO> page(Map<String, Object> params);

    List<ScCurrenthistorySumDTO> list(Map<String, Object> params);

    ScCurrenthistorySumDTO get(String id);

    void save(ScCurrenthistorySumDTO dto);

    void update(ScCurrenthistorySumDTO dto);

    void delete(String[] ids);

    /**查询前十电流值*/
    List<ScCurrenthistorySumEntity>  getByTop();
}