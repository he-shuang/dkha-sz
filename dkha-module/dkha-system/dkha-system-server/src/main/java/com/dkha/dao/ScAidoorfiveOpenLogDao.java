package com.dkha.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScAidoorfiveOpenLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-29
 */
@Mapper
public interface ScAidoorfiveOpenLogDao extends BaseDao<ScAidoorfiveOpenLogEntity> {
    IPage<ScAidoorfiveOpenLogEntity> findPage(IPage<ScAidoorfiveOpenLogEntity> add_date, @Param("params")Map<String, Object> params);

//    List<ScAidoorfiveOpenLogEntity> getMyList(Map<String, Object> params);
}