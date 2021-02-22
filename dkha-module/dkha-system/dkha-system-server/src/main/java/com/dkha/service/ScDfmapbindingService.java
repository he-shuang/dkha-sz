package com.dkha.service;


import com.dkha.commons.mybatis.service.CrudService;
import com.dkha.dto.ScDfmapbindingDTO;
import com.dkha.entity.ScDfmapbindingEntity;

/**
 * 楼层与星网地图绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-29
 */
public interface ScDfmapbindingService extends CrudService<ScDfmapbindingEntity, ScDfmapbindingDTO> {

    /**
     * 通过dfFloorid获取UWB对应绑定信息
     * @param dfFloorid 楼层ID
     */
    ScDfmapbindingEntity getByDfFloorid(Long dfFloorid);
}