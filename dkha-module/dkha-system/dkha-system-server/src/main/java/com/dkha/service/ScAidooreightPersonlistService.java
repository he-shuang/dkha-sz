package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScAidooreightPersonlistDTO;
import com.dkha.entity.ScAidooreightPersonlistEntity;

import java.util.List;
import java.util.Map;

/**
 * 8英寸智能门禁设备具体的人脸信息
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-16
 */
public interface ScAidooreightPersonlistService extends BaseService<ScAidooreightPersonlistEntity> {

    PageData<ScAidooreightPersonlistDTO> page(Map<String, Object> params);

    List<ScAidooreightPersonlistDTO> list(Map<String, Object> params);

    ScAidooreightPersonlistDTO get(String id);

    void save(ScAidooreightPersonlistDTO dto);

    void update(ScAidooreightPersonlistDTO dto);

    void delete(String[] id);

    void insertBatchAndUpdate(List<ScAidooreightPersonlistDTO> personlistDTOS);

    void batchDelete(Map<String, String[]> params);

    boolean delAllByUser(String userNo);
}
