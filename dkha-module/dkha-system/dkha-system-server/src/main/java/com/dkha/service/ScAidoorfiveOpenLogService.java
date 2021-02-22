package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScAidoorfiveOpenLogDTO;
import com.dkha.entity.ScAidoorfiveOpenLogEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-29
 */
public interface ScAidoorfiveOpenLogService extends BaseService<ScAidoorfiveOpenLogEntity> {

    PageData<ScAidoorfiveOpenLogDTO> page(Map<String, Object> params);

    List<ScAidoorfiveOpenLogDTO> list(Map<String, Object> params);

    ScAidoorfiveOpenLogDTO get(String id);

    void save(ScAidoorfiveOpenLogDTO dto);

    void update(ScAidoorfiveOpenLogDTO dto);

    void delete(String[] ids);
}