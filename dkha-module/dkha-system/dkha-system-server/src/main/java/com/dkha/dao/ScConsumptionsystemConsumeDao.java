package com.dkha.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScConsumptionsystemConsumeEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.Map;

/**
 * 消费系统的消费记录
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Mapper
public interface ScConsumptionsystemConsumeDao extends BaseDao<ScConsumptionsystemConsumeEntity> {

    IPage<ScConsumptionsystemConsumeEntity> findPage(@Param("page") IPage<ScConsumptionsystemConsumeEntity> page, @Param("params") Map<String, Object> params);
}