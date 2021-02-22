package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScThermalImagingEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 热成像设备表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
@Mapper
public interface ScThermalImagingDao extends BaseDao<ScThermalImagingEntity> {
	
}