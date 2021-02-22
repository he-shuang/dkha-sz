package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScWorkersarchivesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 教职工档案
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScWorkersarchivesDao extends BaseDao<ScWorkersarchivesEntity> {

    List<ScWorkersarchivesEntity> getByScNos(List<String> empno);

    List<String> getAllId();
}
