package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScStudentsOutHistoryEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 学生未归寝每日统计
 *
 * @author Mark
 * @since v1.0.0 2020-10-09
 */
@Mapper
public interface ScStudentsOutHistoryDao extends BaseDao<ScStudentsOutHistoryEntity> {

    List<ScStudentsOutHistoryEntity> notReturnedStatistics();
}
