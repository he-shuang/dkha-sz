package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScDfmapbindingEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 楼层与星网地图绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-29
 */
@Mapper
public interface ScDfmapbindingDao extends BaseDao<ScDfmapbindingEntity> {

    /**
     * 通过dfFloorid获取UWB对应绑定信息
     * @param dfFloorid 楼层ID
     */
    ScDfmapbindingEntity getByDfFloorid(@Param("dfFloorid") Long dfFloorid);
	
}