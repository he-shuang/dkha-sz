package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScConsumptionsystemVipDTO;
import com.dkha.entity.ScConsumptionsystemVipEntity;

import java.util.List;
import java.util.Map;

/**
 * 消费系统会员信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
public interface ScConsumptionsystemVipService extends BaseService<ScConsumptionsystemVipEntity> {

    PageData<ScConsumptionsystemVipDTO> page(Map<String, Object> params);

    List<ScConsumptionsystemVipDTO> list(Map<String, Object> params);

    ScConsumptionsystemVipDTO get(String id);

    void save(ScConsumptionsystemVipDTO dto);

    void update(ScConsumptionsystemVipDTO dto);

    void delete(String[] ids);
}