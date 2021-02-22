package com.dkha.dao;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScDormitorypersonEntity;
import com.dkha.entity.ScGatewaydcEntity;
import com.dkha.entity.ScTransformerdcEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 网关302设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScGatewaydcDao extends BaseDao<ScGatewaydcEntity> {
   IPage<ScGatewaydcEntity> getPages(@Param("page") IPage<ScGatewaydcEntity> page, @Param("params") Map<String, Object> params);
}
