package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.AccessRecordsDTO;
import com.dkha.entity.AccessRecordsEntity;

import java.util.List;
import java.util.Map;

/**
 * 出入记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-30
 */
public interface AccessRecordsService extends BaseService<AccessRecordsEntity> {

    PageData<AccessRecordsDTO> page(Map<String, Object> params);

    List<AccessRecordsDTO> list(Map<String, Object> params);

    AccessRecordsDTO get(String id);

    void save(AccessRecordsDTO dto);

    void update(AccessRecordsDTO dto);

    void delete(String[] ids);
}
