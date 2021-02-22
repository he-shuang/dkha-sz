package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.AccessRecordsEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 出入记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-30
 */
@Mapper
public interface AccessRecordsDao extends BaseDao<AccessRecordsEntity> {

}
