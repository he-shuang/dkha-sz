package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScTransformerroomDTO;
import com.dkha.entity.ScTransformerroomEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 互感器宿舍关联关系
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScTransformerroomDao extends BaseDao<ScTransformerroomEntity> {

    /**
     * 根据电流互感器设备ID删除所关联的房间信息
     */
    void deleteByGateId(String tfId);

    /**
     * 根据电流互感器ID查询所关联的房间关联数据
     */
    List<ScTransformerroomDTO> findAllById(String id);

    /**
     * 根据传入的房间关联ID查询对应的房间编码
     */
    ScTransformerroomEntity selectRoomById(Long drId);

    void deleteByNull();

    List<ScTransformerroomEntity> selectByTfId(Long tfId);
}
