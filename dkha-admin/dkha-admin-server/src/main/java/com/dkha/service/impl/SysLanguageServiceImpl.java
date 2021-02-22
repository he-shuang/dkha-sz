

package com.dkha.service.impl;

import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.dao.SysLanguageDao;
import com.dkha.entity.SysLanguageEntity;
import com.dkha.service.SysLanguageService;
import org.springframework.stereotype.Service;

/**
 * 国际化
 */
@Service
public class SysLanguageServiceImpl extends BaseServiceImpl<SysLanguageDao, SysLanguageEntity> implements SysLanguageService {

    @Override
    public void saveOrUpdate(String tableName, Long tableId, String fieldName, String fieldValue, String language) {
        SysLanguageEntity entity = new SysLanguageEntity();
        entity.setTableName(tableName);
        entity.setTableId(tableId);
        entity.setFieldName(fieldName);
        entity.setFieldValue(fieldValue);
        entity.setLanguage(language);

        //判断是否有数据
        if(baseDao.getLanguage(entity) == null){
            baseDao.insert(entity);
        }else {
            baseDao.updateLanguage(entity);
        }
    }
}
