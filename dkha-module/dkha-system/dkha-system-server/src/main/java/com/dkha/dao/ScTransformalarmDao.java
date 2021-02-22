package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScTransformalarmEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 电流互感器房间电流信息报警
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScTransformalarmDao extends BaseDao<ScTransformalarmEntity> {

    /**
     * 获取分页数据
     * @param params
     * @return
     */
    List<ScTransformalarmEntity> getMyList(Map<String, Object> params);

    /**
     * 获取分页总数
     * @param params
     * @return
     */
    Long getMyCount(Map<String, Object> params);
}
