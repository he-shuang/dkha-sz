package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.redis.RedisUtils;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScGatebusdeviceDao;
import com.dkha.dto.ScGatebusdeviceDTO;
import com.dkha.dto.ScGatewaydcDTO;
import com.dkha.entity.ScGatebusdeviceEntity;
import com.dkha.service.ScGateWayModBusDataChangeService;
import com.dkha.service.ScGatebusdeviceService;
import com.dkha.service.ScGatewaydcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 485通讯总线下挂载的设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScGatebusdeviceServiceImpl extends BaseServiceImpl<ScGatebusdeviceDao, ScGatebusdeviceEntity> implements ScGatebusdeviceService {


    @Autowired
    ScGateWayModBusDataChangeService scGateWayModBusDataChangeService;



    @Override
    public PageData<ScGatebusdeviceDTO> page(Map<String, Object> params) {
        IPage<ScGatebusdeviceEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScGatebusdeviceDTO.class);
    }

    @Override
    public List<ScGatebusdeviceDTO> list(Map<String, Object> params) {
        List<ScGatebusdeviceEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScGatebusdeviceDTO.class);
    }

    private QueryWrapper<ScGatebusdeviceEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<ScGatebusdeviceEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScGatebusdeviceDTO get(String id) {
        ScGatebusdeviceEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScGatebusdeviceDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScGatebusdeviceDTO dto) {
        ScGatebusdeviceEntity entity = ConvertUtils.sourceToTarget(dto, ScGatebusdeviceEntity.class);
        if(insert(entity))
        {
            scGateWayModBusDataChangeService.sendGateWayModBusChange(dto.getGwId(),true);
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScGatebusdeviceDTO dto) {
        ScGatebusdeviceEntity entity = ConvertUtils.sourceToTarget(dto, ScGatebusdeviceEntity.class);

        if(updateById(entity))
        {
            scGateWayModBusDataChangeService.sendGateWayModBusChange(dto.getGwId(),true);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScGatebusdeviceEntity.class);

        for (String id:ids) {

            ScGatebusdeviceEntity scGatebusdc=  baseDao.selectById(id);
            if(scGatebusdc!=null){
                scGateWayModBusDataChangeService.sendGateWayModBusChange(scGatebusdc.getGwId(),true);
            }
            baseDao.deleteById(id);
        }
    }

    @Override
    public List<ScGatebusdeviceDTO> findAllById(String id) {
        return baseDao.findAllById(id);
    }

    @Override
    public void deleteByGateId(String gbdId) {
        QueryWrapper<ScGatebusdeviceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gbd_id",gbdId);
        Integer integer = baseDao.selectCount(queryWrapper);
        if(integer > 0){
            scGateWayModBusDataChangeService.sendGateWayModBusChange(gbdId,true);
            baseDao.deleteByGateId(gbdId);
        }

    }




}
