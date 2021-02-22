package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScTransformerdcDTO;
import com.dkha.entity.ScTransformerdcEntity;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.Map;

/**
 * 互感器设备信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScTransformerdcService extends BaseService<ScTransformerdcEntity> {

    PageData<ScTransformerdcDTO> page(Map<String, Object> params);

    List<ScTransformerdcDTO> list(Map<String, Object> params);

    ScTransformerdcDTO get(String id);

    void save(ScTransformerdcDTO dto);

    void update(ScTransformerdcDTO dto);

    void delete(String id);

    /**
     * 电流互感器信息导入
     */
    void importInfoExcel(MultipartFile file);

    ScTransformerdcDTO info(String id);
}
