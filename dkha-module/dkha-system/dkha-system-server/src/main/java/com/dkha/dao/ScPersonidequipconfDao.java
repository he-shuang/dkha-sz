package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScPersonidequipconfEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人证配置信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Mapper
public interface ScPersonidequipconfDao extends BaseDao<ScPersonidequipconfEntity> {

    ScPersonidequipconfEntity getNewInfo();

}
