package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScGatewaydcDTO;
import com.dkha.dto.ScTransformerdcDTO;
import com.dkha.entity.ScGatewaydcEntity;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

/**
 * 网关302设备信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScGatewaydcService extends BaseService<ScGatewaydcEntity> {

    PageData<ScGatewaydcDTO> page(Map<String, Object> params);

    List<ScGatewaydcDTO> list(Map<String, Object> params);

    ScGatewaydcDTO get(String id);

    void save(ScGatewaydcDTO dto);

    void update(ScGatewaydcDTO dto);

    void delete(String id);
    /**
     * 网关302设备详情
     */
    ScGatewaydcDTO getInfo(String id);

    /**
     * 网关302设备信息
     * @param file
     */
    void importExcel (MultipartFile file) throws Exception;
}
