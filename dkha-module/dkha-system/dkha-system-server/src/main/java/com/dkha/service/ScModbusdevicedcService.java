package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScModbusdevicedcDTO;
import com.dkha.entity.ScModbusdevicedcEntity;
import com.dkha.excel.ScModbusdevicedcExcel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScModbusdevicedcService extends BaseService<ScModbusdevicedcEntity> {

    PageData<ScModbusdevicedcDTO> page(Map<String, Object> params);

    List<ScModbusdevicedcDTO> list(Map<String, Object> params);

    ScModbusdevicedcDTO get(String id);

    void save(ScModbusdevicedcDTO dto);

    void update(ScModbusdevicedcDTO dto);

    void delete(String ids);

    /**
     * 根据设备类型获取设备地址
     */
    List<ScModbusdevicedcDTO> getTypeMessage(String type);

    /**
     * 根据与网管关联情况更新组网状态
     */
    void updateNetwork(Long mbdId);

    ScModbusdevicedcDTO info(String id);

    /**
     * 导入PIR设备信息
     * @param file
     */
    void importPIRExcel (MultipartFile file) throws Exception;

    /**
     * 导入PM设备信息
     * @param file
     */
    void importPMExcel (MultipartFile file) throws Exception;

    /**
     * 导入SL设备信息
     * @param file
     */
    void importSLExcel (MultipartFile file) throws Exception;

    void updateBatchNetwork(int network, List<Long> ids);
}
