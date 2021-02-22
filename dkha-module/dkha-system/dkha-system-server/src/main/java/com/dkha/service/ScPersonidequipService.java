

package com.dkha.service;


import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScPersonidequipDTO;
import com.dkha.entity.ScPersonidequipEntity;

import java.util.List;
import java.util.Map;

/**
 * 人证识别设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
public interface ScPersonidequipService extends BaseService<ScPersonidequipEntity> {

    PageData<ScPersonidequipDTO> page(Map<String, Object> params);

    List<ScPersonidequipDTO> list(Map<String, Object> params);

    ScPersonidequipDTO get(String id);

    void save(ScPersonidequipDTO dto);

    void update(ScPersonidequipDTO dto);

    void delete(String id);

    List<ScPersonidequipDTO> getAll();
}
