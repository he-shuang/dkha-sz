package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScFaceverificationDTO;
import com.dkha.entity.ScFaceverificationEntity;

import java.util.List;
import java.util.Map;

/**
 * 刷脸或卡记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
public interface ScFaceverificationService extends BaseService<ScFaceverificationEntity> {

    PageData<ScFaceverificationDTO> page(Map<String, Object> params);

    List<ScFaceverificationDTO> list(Map<String, Object> params);

    ScFaceverificationDTO get(String id);
}