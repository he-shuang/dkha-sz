package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScAidooreightPersonlistDTO;
import com.dkha.entity.ScAidooreightPersonlistEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 8英寸智能门禁设备具体的人脸信息
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-16
 */
@Mapper
public interface ScAidooreightPersonlistDao extends BaseDao<ScAidooreightPersonlistEntity> {

    void insertBatchAndUpdate(List<ScAidooreightPersonlistEntity> personlistDTOS);
}
