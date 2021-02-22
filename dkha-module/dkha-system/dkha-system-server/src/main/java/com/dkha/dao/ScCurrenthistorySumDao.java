package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScCurrenthistorySumEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-11-06
 */
@Mapper
public interface ScCurrenthistorySumDao extends BaseDao<ScCurrenthistorySumEntity> {
    /**查询前十电流值*/
    List<ScCurrenthistorySumEntity>  getByTop(@Param("startTime")String startTime);
}