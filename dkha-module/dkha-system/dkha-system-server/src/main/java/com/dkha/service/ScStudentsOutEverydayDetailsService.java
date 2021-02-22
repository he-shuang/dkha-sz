package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScStudentsCountDTO;
import com.dkha.dto.ScStudentsOutEverydayDetailsDTO;
import com.dkha.entity.ScStudentsOutEverydayDetailsEntity;

import java.util.List;
import java.util.Map;

/**
 * 每日学生未归详情
 *
 * @author Mark 
 * @since v1.0.0 2020-10-15
 */
public interface ScStudentsOutEverydayDetailsService extends BaseService<ScStudentsOutEverydayDetailsEntity> {

    PageData<ScStudentsOutEverydayDetailsDTO> page(Map<String, Object> params);

    PageData<ScStudentsCountDTO> findCountSum(Map<String, Object> params);
    PageData<ScStudentsCountDTO> findCountRoomSum(Map<String, Object> params);
    List<ScStudentsCountDTO> findList(Map<String, Object> params);
    List<ScStudentsCountDTO>  findCountRoomExcel( Map<String, Object> params);

}