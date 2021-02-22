package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScOccupancyhistoryEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 某房间的入住历史记录
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScOccupancyhistoryDao extends BaseDao<ScOccupancyhistoryEntity> {

}
