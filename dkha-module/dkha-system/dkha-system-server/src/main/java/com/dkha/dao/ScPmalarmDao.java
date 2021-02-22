package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScPmalarmEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * PM2.5设备报警信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScPmalarmDao extends BaseDao<ScPmalarmEntity> {
    /**
     * 获取分页数据
     * @param params
     * @return
     */
    List<ScPmalarmEntity> getMyList(Map<String, Object> params);

    /**
     * 获取分页总数
     * @param params
     * @return
     */
    Long getMyCount(Map<String, Object> params);
}
