package com.dkha.dao;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScUwbalarmEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * uwb报警内容：工具标签报警，访客禁区报警，保密区域报警
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScUwbalarmDao extends BaseDao<ScUwbalarmEntity> {
    /**
     * 获取分页数据
     * @param params
     * @return
     */
    IPage<ScUwbalarmEntity> getMyList(@Param("page") IPage<ScUwbalarmEntity> page,  @Param("params")Map<String, Object> params);

    /**
     * 获取分页总数
     * @param params
     * @return
     */
    Long getMyCount(Map<String, Object> params);
}
