package com.dkha.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScConsumptionsystemRechargeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 消费系统充值信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Mapper
public interface ScConsumptionsystemRechargeDao extends BaseDao<ScConsumptionsystemRechargeEntity> {

    IPage<ScConsumptionsystemRechargeEntity> findPage(@Param("page") IPage<ScConsumptionsystemRechargeEntity> page, @Param("params") Map<String, Object> params);
}