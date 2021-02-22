package com.dkha.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScAidooreightEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 智能设备主要分为：8英寸智能门禁设备
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-14
 */
@Mapper
public interface ScAidooreightDao extends BaseDao<ScAidooreightEntity> {

    IPage<ScAidooreightEntity> findPage(@Param("ae_setupdate") IPage<ScAidooreightEntity> ae_setupdate, @Param("params") Map<String, Object> params);
    List<ScAidooreightEntity> listNoStopDevice();

}