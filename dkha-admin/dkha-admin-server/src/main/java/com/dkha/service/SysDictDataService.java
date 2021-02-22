

package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.SysDictDataDTO;
import com.dkha.entity.SysDictDataEntity;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 */
public interface SysDictDataService extends BaseService<SysDictDataEntity> {

    PageData<SysDictDataDTO> page(Map<String, Object> params);

    SysDictDataDTO get(Long id);

    void save(SysDictDataDTO dto);

    void update(SysDictDataDTO dto);

    void delete(Long[] ids);

    List<SysDictDataDTO> getByType(String type);
}
