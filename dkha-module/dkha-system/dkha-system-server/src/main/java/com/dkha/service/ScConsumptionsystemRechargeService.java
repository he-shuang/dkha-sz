package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScConsumptionsystemRechargeDTO;
import com.dkha.entity.ScConsumptionsystemRechargeEntity;

import java.util.List;
import java.util.Map;

/**
 * 消费系统充值信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
public interface ScConsumptionsystemRechargeService extends BaseService<ScConsumptionsystemRechargeEntity> {

    PageData<ScConsumptionsystemRechargeDTO> page(Map<String, Object> params);

    List<ScConsumptionsystemRechargeDTO> list(Map<String, Object> params);

    ScConsumptionsystemRechargeDTO get(String id);

    void save(ScConsumptionsystemRechargeDTO dto);

    void update(ScConsumptionsystemRechargeDTO dto);

    void delete(String[] ids);
}