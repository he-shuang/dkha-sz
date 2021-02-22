package com.dkha.service;


import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScDormitoryDTO;
import com.dkha.entity.ScDormitoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 房间及房间状态信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScDormitoryService extends BaseService<ScDormitoryEntity> {

    PageData<ScDormitoryDTO> page(Map<String, Object> params);

    List<ScDormitoryDTO> list(Map<String, Object> params);

    ScDormitoryDTO get(String id);

    void save(ScDormitoryDTO dto);

    void update(ScDormitoryDTO dto);

    void delete(String[] ids);
}
