package com.dkha.service;



import com.dkha.commons.mybatis.service.BaseService;
import com.dkha.commons.tools.page.PageData;
import com.dkha.dto.ScGatebusdeviceDTO;
import com.dkha.entity.ScGatebusdeviceEntity;


import java.util.List;
import java.util.Map;

/**
 * 485通讯总线下挂载的设备信息
 *
 * @since v1.0.0 2020-08-23
 */
public interface ScGatebusdeviceService extends BaseService<ScGatebusdeviceEntity> {

    PageData<ScGatebusdeviceDTO> page(Map<String, Object> params);

    List<ScGatebusdeviceDTO> list(Map<String, Object> params);

    ScGatebusdeviceDTO get(String id);

    void save(ScGatebusdeviceDTO dto);

    void update(ScGatebusdeviceDTO dto);

    void delete(String[] ids);

    /**
     * 根据302网关设备ID查询所有有关的RS485总线
     */
    List<ScGatebusdeviceDTO> findAllById(String id);

    /**
     * 根据302网关ID删除关联的RS485总线
     */
    void deleteByGateId(String gbdId);
}
