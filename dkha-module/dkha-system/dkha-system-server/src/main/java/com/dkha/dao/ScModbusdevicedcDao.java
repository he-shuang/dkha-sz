package com.dkha.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScModbusdevicedcDTO;
import com.dkha.entity.ScGatewaydcEntity;
import com.dkha.entity.ScModbusdevicedcEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScModbusdevicedcDao extends BaseDao<ScModbusdevicedcEntity> {

    /**
     * 根据设备类型查询到相关设备
     */
    List<ScModbusdevicedcDTO> getTypeMessage(String type);

    void updateNetwork(Long mbdId);

    void updateBatchNetwork(@Param("network") int network, @Param("ids") List<Long> ids);
    IPage<ScModbusdevicedcEntity> getPages(@Param("page") IPage<ScModbusdevicedcEntity> page, @Param("params") Map<String, Object> params);
}
