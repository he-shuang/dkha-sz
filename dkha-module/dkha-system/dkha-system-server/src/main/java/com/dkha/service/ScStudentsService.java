package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScStudentsDTO;
import com.dkha.entity.ScStudentsEntity;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

/**
 * 学生档案信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScStudentsService extends BaseService<ScStudentsEntity> {

    PageData<ScStudentsDTO> page(Map<String, Object> params);

    List<ScStudentsDTO> list(Map<String, Object> params);

    ScStudentsDTO get(String id);

    void save(ScStudentsDTO dto);

    void update(ScStudentsDTO dto);

    void delete(Long ids);

    void importInfoExcel(MultipartFile file);

    void importImg(MultipartFile[] file);

    void importRegisterInfoExcel(MultipartFile file);

    List<ScStudentsDTO> getStudentsInfo(List<Long> id);

    List<ScStudentsDTO> getStudentsByIds(List<Long> id);

    List<String> getAllId();
}
