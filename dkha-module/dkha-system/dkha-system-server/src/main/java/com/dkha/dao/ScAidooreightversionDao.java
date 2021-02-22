
package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScAidooreightversionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-21
 */
@Mapper
public interface ScAidooreightversionDao extends BaseDao<ScAidooreightversionEntity> {
    ScAidooreightversionEntity getLastVersionByType(@Param("mainboard") Integer mainboard);
}