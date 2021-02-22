package com.dkha.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScThermalAlarmEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;


/**
 * 热成像报警表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
@Mapper
public interface ScThermalAlarmDao extends BaseDao<ScThermalAlarmEntity> {


    Page<ScThermalAlarmEntity> getThermalAlarmByPage(@Param("page") IPage page, @Param(Constants.WRAPPER) QueryWrapper ew, @Param("params") Map<String, Object> params);

	
}