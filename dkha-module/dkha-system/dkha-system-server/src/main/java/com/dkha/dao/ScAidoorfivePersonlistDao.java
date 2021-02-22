package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScAidoorfivePersonlistEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-16
 */
@Mapper
public interface ScAidoorfivePersonlistDao extends BaseDao<ScAidoorfivePersonlistEntity> {

    Long getPersonNum(@Param("fSerial") String fSerial,@Param("fPassword") String fPassword);
}