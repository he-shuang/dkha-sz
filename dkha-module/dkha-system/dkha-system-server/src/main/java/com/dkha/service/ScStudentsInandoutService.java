package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScRoomCountDTO;
import com.dkha.dto.ScStudentsInandoutDTO;
import com.dkha.entity.ScStudentsInandoutEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author dkha 
 * @since v1.0.0 2020-10-22
 */
public interface ScStudentsInandoutService extends BaseService<ScStudentsInandoutEntity> {

    PageData<ScStudentsInandoutDTO> page(Map<String, Object> params);

    List<ScStudentsInandoutDTO> list(Map<String, Object> params);

    ScStudentsInandoutDTO get(String id);

    void save(ScStudentsInandoutDTO dto);

    void update(ScStudentsInandoutDTO dto);

    void delete(String[] ids);
}