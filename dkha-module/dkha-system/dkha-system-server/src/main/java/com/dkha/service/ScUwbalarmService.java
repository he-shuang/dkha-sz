package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScUwbalarmDTO;
import com.dkha.entity.ScUwbalarmEntity;


import java.util.List;
import java.util.Map;

/**
 * uwb报警内容：工具标签报警，访客禁区报警，保密区域报警
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScUwbalarmService extends BaseService<ScUwbalarmEntity> {

    PageData<ScUwbalarmDTO> page(Map<String, Object> params);

    List<ScUwbalarmDTO> list(Map<String, Object> params);

    ScUwbalarmDTO get(String id);

    void save(ScUwbalarmDTO dto);

    void update(ScUwbalarmDTO dto);

    void delete(String[] ids);
}
