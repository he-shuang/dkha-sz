package com.dkha.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScGatewaydcEntity;
import com.dkha.entity.ScImportantDeviceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 重要设备信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Mapper
public interface ScImportantDeviceDao extends BaseDao<ScImportantDeviceEntity> {
    IPage<ScImportantDeviceEntity> getPages(@Param("page") IPage<ScImportantDeviceEntity> page, @Param("params") Map<String, Object> params);
}