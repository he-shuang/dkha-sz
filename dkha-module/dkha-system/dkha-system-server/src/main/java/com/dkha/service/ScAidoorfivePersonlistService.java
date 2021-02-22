package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScAidoorfivePersonlistDTO;
import com.dkha.entity.ScAidoorfivePersonlistEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-16
 */
public interface ScAidoorfivePersonlistService extends BaseService<ScAidoorfivePersonlistEntity> {

    PageData<ScAidoorfivePersonlistDTO> page(Map<String, Object> params);

    List<ScAidoorfivePersonlistDTO> list(Map<String, Object> params);

    ScAidoorfivePersonlistDTO get(String id);

    void save(ScAidoorfivePersonlistDTO dto);

    void update(ScAidoorfivePersonlistDTO dto);

    void delete(String[] ids);
}