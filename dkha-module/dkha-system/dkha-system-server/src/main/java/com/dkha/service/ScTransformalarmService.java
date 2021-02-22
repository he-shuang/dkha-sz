package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScTransformalarmDTO;
import com.dkha.entity.ScTransformalarmEntity;


import java.util.List;
import java.util.Map;

/**
 * 电流互感器房间电流信息报警
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScTransformalarmService extends BaseService<ScTransformalarmEntity> {

    PageData<ScTransformalarmDTO> page(Map<String, Object> params);

    List<ScTransformalarmDTO> list(Map<String, Object> params);

    ScTransformalarmDTO get(String id);

    void save(ScTransformalarmDTO dto);

    void update(ScTransformalarmDTO dto);

    void delete(String[] ids);
}
