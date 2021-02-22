package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScAidooreightDailyDTO;
import com.dkha.entity.ScAidooreightDailyEntity;

import java.util.List;
import java.util.Map;

/**
 * 8英寸智能门禁设备每日采集数量
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
public interface ScAidooreightDailyService extends BaseService<ScAidooreightDailyEntity> {

    PageData<ScAidooreightDailyDTO> page(Map<String, Object> params);

    List<ScAidooreightDailyDTO> list(Map<String, Object> params);

    ScAidooreightDailyDTO get(String id);
}