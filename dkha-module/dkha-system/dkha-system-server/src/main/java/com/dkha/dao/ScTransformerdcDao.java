package com.dkha.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScTransformerdcEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 互感器设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScTransformerdcDao extends BaseDao<ScTransformerdcEntity> {

    /**
     * 分页查询
     */
    IPage<ScTransformerdcEntity> findPage(@Param("tf_setupdate")IPage<ScTransformerdcEntity> tf_setupdate,@Param("params") Map<String, Object> params);
}
