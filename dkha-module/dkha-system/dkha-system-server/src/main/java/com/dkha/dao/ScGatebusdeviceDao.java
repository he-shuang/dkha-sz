package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScGatebusdeviceDTO;
import com.dkha.entity.ScGatebusdeviceEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 485通讯总线下挂载的设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScGatebusdeviceDao extends BaseDao<ScGatebusdeviceEntity> {

    /**
     * 根据302网关设备ID获取所有RS485总线
     */
    List<ScGatebusdeviceDTO> findAllById(String id);

    /**
     * 根据302网关设备ID删除所有关联的RS485总线
     */
    void deleteByGateId(String gbdId);
}
