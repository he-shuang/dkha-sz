package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScGateguarddcDTO;
import com.dkha.entity.ScGateguarddcEntity;


import java.util.List;
import java.util.Map;

/**
 *
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScGateguarddcService extends BaseService<ScGateguarddcEntity> {

    PageData<ScGateguarddcDTO> page(Map<String, Object> params);

    List<ScGateguarddcDTO> list(Map<String, Object> params);

    ScGateguarddcDTO get(String id);

    void save(ScGateguarddcDTO dto);

    void update(ScGateguarddcDTO dto);

    void delete(String[] ids);
}
