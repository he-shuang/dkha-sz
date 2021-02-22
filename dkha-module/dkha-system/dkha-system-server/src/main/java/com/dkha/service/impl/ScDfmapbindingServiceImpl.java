package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dkha.commons.mybatis.service.impl.CrudServiceImpl;
import com.dkha.dao.ScDfmapbindingDao;
import com.dkha.dto.ScDfmapbindingDTO;
import com.dkha.entity.ScDfmapbindingEntity;
import com.dkha.service.ScDfmapbindingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 楼层与星网地图绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-29
 */
@Service
public class ScDfmapbindingServiceImpl extends CrudServiceImpl<ScDfmapbindingDao, ScDfmapbindingEntity, ScDfmapbindingDTO> implements ScDfmapbindingService {

    @Override
    public QueryWrapper<ScDfmapbindingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScDfmapbindingEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }


    @Override
    public ScDfmapbindingEntity getByDfFloorid(Long dfFloorid) {
        return baseDao.getByDfFloorid(dfFloorid);
    }
}