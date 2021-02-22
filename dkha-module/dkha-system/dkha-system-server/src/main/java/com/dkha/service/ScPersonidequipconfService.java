

package com.dkha.service;


import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScPersonidequipconfDTO;
import com.dkha.entity.ScPersonidequipconfEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 人证配置信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
public interface ScPersonidequipconfService extends BaseService<ScPersonidequipconfEntity> {

    PageData<ScPersonidequipconfDTO> page(Map<String, Object> params);

    List<ScPersonidequipconfDTO> list(Map<String, Object> params);

    ScPersonidequipconfDTO get(String id);

    void save(ScPersonidequipconfDTO dto);

    void update(ScPersonidequipconfDTO dto);

    void delete(Long ids);

    ScPersonidequipconfDTO getByEquipsn(String equipsn);

    /**
     * 导入人证设备信息
     * @param file
     */
    void importExcel (MultipartFile file) throws Exception;
}
