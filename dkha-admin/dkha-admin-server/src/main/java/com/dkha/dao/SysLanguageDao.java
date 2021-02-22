

package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.SysLanguageEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 国际化
 *
 *
 */
@Mapper
public interface SysLanguageDao extends BaseDao<SysLanguageEntity> {

    SysLanguageEntity getLanguage(SysLanguageEntity entity);

    void updateLanguage(SysLanguageEntity entity);

}
