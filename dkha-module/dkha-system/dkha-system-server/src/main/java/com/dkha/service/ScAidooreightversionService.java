
package com.dkha.service;

import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.doorcontrol.ScAidooreightversionDTO;
import com.dkha.entity.ScAidooreightversionEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface ScAidooreightversionService extends BaseService<ScAidooreightversionEntity> {

    PageData<ScAidooreightversionDTO> page(Map<String, Object> params);

    List<ScAidooreightversionDTO> list(Map<String, Object> params);

    ScAidooreightversionDTO get(String id);

    void save(ScAidooreightversionDTO dto);

    void update(ScAidooreightversionDTO dto);

    void delete(String[] ids);

    String uploadNewVersionAPKfile(MultipartFile file);

    ScAidooreightversionEntity getLastVersionByType(Integer mainboard);
}