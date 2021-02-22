package com.dkha.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.mybatis.service.CrudService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScGatewaydcDTO;
import com.dkha.dto.ScImportantDeviceDTO;
import com.dkha.entity.ScGatewaydcEntity;
import com.dkha.entity.ScImportantDeviceEntity;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * 重要设备信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
public interface ScImportantDeviceService extends CrudService<ScImportantDeviceEntity, ScImportantDeviceDTO> {
    @Override
    PageData<ScImportantDeviceDTO> page(Map<String, Object> params);

    @Override
    void save(ScImportantDeviceDTO dto);

    @Override
    ScImportantDeviceDTO get(Long id);

    @Override
    void update(ScImportantDeviceDTO dto);

    @Override
    boolean deleteById(Serializable id);

    /**
     * 导入重要设备信息
     * @param file
     */
    void importExcel (MultipartFile file) throws Exception;

}