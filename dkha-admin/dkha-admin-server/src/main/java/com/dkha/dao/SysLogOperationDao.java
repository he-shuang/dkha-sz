

package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.SysLogOperationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 操作日志
 * @since 1.0.0
 */
@Mapper
public interface SysLogOperationDao extends BaseDao<SysLogOperationEntity> {

}
