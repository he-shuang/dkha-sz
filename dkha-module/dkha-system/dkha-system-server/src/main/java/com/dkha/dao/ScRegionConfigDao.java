package com.dkha.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScRegionConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 区域配置/uwb围栏关联
 *
 * @author Mark
 * @since v1.0.0 2020-09-01
 */
@Mapper
public interface ScRegionConfigDao extends BaseDao<ScRegionConfigEntity> {

    IPage<ScRegionConfigEntity> pageList(@Param("page") IPage<ScRegionConfigEntity> page,@Param("params") Map<String, Object> params);
}
