package com.dkha.service;


import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScStudentsNotInSchoolDTO;
import com.dkha.dto.ScStudentsNotInSchoolDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生未归寝每日统计 服务类
 * </p>
 *
 * @author Spring
 * @since 2020-12-07
 */
public interface ScStudentsNotInSchoolService {

    PageData<ScStudentsNotInSchoolDTO> page(Map<String, Object> params);

    ScStudentsNotInSchoolDTO get(String id);

    void save(ScStudentsNotInSchoolDTO dto);

    void update(ScStudentsNotInSchoolDTO dto);

    void delete(String[] ids);

    List<ScStudentsNotInSchoolDTO> list(Map<String, Object> params);

    ScStudentsNotInSchoolDTO getTest();
}
