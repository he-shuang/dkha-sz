package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScThermalAlarmDao;
import com.dkha.dto.ScThermalAlarmDTO;
import com.dkha.entity.ScThermalAlarmEntity;
import com.dkha.service.ScThermalAlarmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 热成像报警表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
@Service
public class ScThermalAlarmServiceImpl extends BaseServiceImpl<ScThermalAlarmDao, ScThermalAlarmEntity> implements ScThermalAlarmService {


    @Override
    public PageData<ScThermalAlarmDTO> page(Map<String, Object> params) {
        IPage<ScThermalAlarmEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        PageData<ScThermalAlarmDTO> pageData = getPageData(page, ScThermalAlarmDTO.class);
        List<ScThermalAlarmDTO> list = pageData.getList();
        if(list.size()>0){
            for (ScThermalAlarmDTO sScThermalAlarmDTO : list) {


            }
        }
        return getPageData(page, ScThermalAlarmDTO.class);
    }

    @Override
    public List<ScThermalAlarmDTO> list(Map<String, Object> params) {
        List<ScThermalAlarmEntity> entityList = baseDao.selectList(getWrapper(params));
        return ConvertUtils.sourceToTarget(entityList, ScThermalAlarmDTO.class);
    }

    private QueryWrapper<ScThermalAlarmEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        String tfDevicename = (String)params.get("tfDevicename");
        String tfIpgateway = (String)params.get("tfIpgateway");
        QueryWrapper<ScThermalAlarmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(tfDevicename), "tf_devicename", tfDevicename);
        wrapper.like(StringUtils.isNotBlank(tfIpgateway), "tf_ipgateway", tfIpgateway);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScThermalAlarmDTO get(String id) {
        ScThermalAlarmEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScThermalAlarmDTO.class);
    }

    @Override
    public PageData<ScThermalAlarmDTO> getThermalAlarmByPage(Map<String, Object> params) {
        //IPage<ScThermalAlarmEntity> page = new Page(1,10);
        IPage<ScThermalAlarmEntity> page = baseDao.getThermalAlarmByPage( getPage(params,
                Constant.CREATE_DATE, false),getWrapper(params),params);
        //PageData<ScThermalAlarmDTO> pageData = getPageData(page, ScThermalAlarmDTO.class);
        return getPageData(page, ScThermalAlarmDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScThermalAlarmDTO dto) {
        ScThermalAlarmEntity entity = ConvertUtils.sourceToTarget(dto, ScThermalAlarmEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScThermalAlarmDTO dto) {
        ScThermalAlarmEntity entity = ConvertUtils.sourceToTarget(dto, ScThermalAlarmEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScThermalAlarmEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}