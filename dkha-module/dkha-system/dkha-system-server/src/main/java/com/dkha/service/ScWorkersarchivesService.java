package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScWorkersarchivesDTO;
import com.dkha.entity.ScWorkersarchivesEntity;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

/**
 * 教职工档案
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScWorkersarchivesService extends BaseService<ScWorkersarchivesEntity> {

    PageData<ScWorkersarchivesDTO> page(Map<String, Object> params);

    List<ScWorkersarchivesDTO> list(Map<String, Object> params);

    ScWorkersarchivesDTO get(String id);

    void save(ScWorkersarchivesDTO dto);

    void update(ScWorkersarchivesDTO dto);

    void delete(Long ids);

    void importImg(MultipartFile[] file);

    void importInfoExcel(MultipartFile file);

    void importRegisterInfoExcel(MultipartFile file);

    List<ScWorkersarchivesDTO> getWorkesByIds(List<Long> ids);

    List<String> getAllId();
}
