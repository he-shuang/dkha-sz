package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.redis.RedisUtils;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.commons.tools.utils.DateUtils;
import com.dkha.dao.ScThermalImagingDao;
import com.dkha.dto.ScThermalImagingDTO;
import com.dkha.entity.ScThermalImagingEntity;
import com.dkha.service.ScThermalImagingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 热成像设备表
 *
 * @author Mark
 * @since v1.0.0 2020-11-04
 */
@Service
public class ScThermalImagingServiceImpl extends BaseServiceImpl<ScThermalImagingDao, ScThermalImagingEntity> implements ScThermalImagingService {
    @Autowired
    private ScDormitoryfloorServiceImpl scDormitoryfloorService;

    @Autowired
    private RedisUtils redisUtils;
    @Override
    public PageData<ScThermalImagingDTO> page(Map<String, Object> params) {
        IPage<ScThermalImagingEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );
        PageData<ScThermalImagingDTO> pageData = getPageData(page, ScThermalImagingDTO.class);
        List<ScThermalImagingDTO> list = pageData.getList();
        int i = 0;
        for (ScThermalImagingDTO scTransformerdcDTO : list) {
            int j = 0;
            if(null != page.getRecords().get(i).getTfSetupaddr()) {
                String[] split = page.getRecords().get(i).getTfSetupaddr().split(",");
                for (String s : split) {
                    String name = scDormitoryfloorService.findName(s);
                    Arrays.fill(split, j, j + 1, name);
                    j++;
                }
                scTransformerdcDTO.setTfSetupaddr(split);
                i++;
            }
        }
        return pageData;
    }

    @Override
    public List<ScThermalImagingDTO> list(Map<String, Object> params) {
        List<ScThermalImagingEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScThermalImagingDTO.class);
    }

    private QueryWrapper<ScThermalImagingEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");
        String tfDevicename = (String)params.get("tfDevicename");
        String tfStatus = (String)params.get("tfStatus");

        QueryWrapper<ScThermalImagingEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(id), "id", id);
        wrapper.like(StringUtils.isNotBlank(tfDevicename), "tf_devicename", tfDevicename);
        wrapper.eq(StringUtils.isNotBlank(tfStatus), "tf_status", tfStatus);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScThermalImagingDTO get(String id) {
        ScThermalImagingEntity entity = baseDao.selectById(id);
        //根据ID查询电流互感器相关的数据
        ScThermalImagingDTO scThermalImagingDTO = ConvertUtils.sourceToTarget(entity, ScThermalImagingDTO.class);
        scThermalImagingDTO.setTfSetupaddr(entity.getTfSetupaddr().split(","));
        return scThermalImagingDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScThermalImagingDTO dto) {
        ScThermalImagingEntity entity = ConvertUtils.sourceToTarget(dto, ScThermalImagingEntity.class);
        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getTfSetupaddr();
        for (String addr : tfSetupaddr) {
            entity.setTfSetupaddr(entity.getTfSetupaddr() == null || entity.getTfSetupaddr().trim().length() == 0 ?
                    addr :
                    entity.getTfSetupaddr() + "," + addr);
        }
        entity.setTfSetupdate(new Date());
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScThermalImagingDTO dto) {
        ScThermalImagingEntity entity = ConvertUtils.sourceToTarget(dto, ScThermalImagingEntity.class);
        //获取数组形式的楼层ID与房间ID并且以逗号形式隔开
        String[] tfSetupaddr = dto.getTfSetupaddr();
        for (String addr : tfSetupaddr) {
            entity.setTfSetupaddr(entity.getTfSetupaddr() == null || entity.getTfSetupaddr().trim().length() == 0 ?
                    addr :
                    entity.getTfSetupaddr() + "," + addr);
        }
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String ids) {
        //逻辑删除
        //logicDelete(ids, ScThermalImagingEntity.class);

        //物理删除
        baseDao.deleteById(ids);
    }

    @Override
    public List<Map<String, Object>> thermalList(String type,Map<String, Object> params) {
        //宿舍 1 教学楼 2
        String tfDevicename = (String)params.get("tfDevicename");
        String tfIpgateway = (String)params.get("tfIpgateway");
        QueryWrapper<ScThermalImagingEntity> queryWrapper = new QueryWrapper<>();
        if("1".equals(type)){
            queryWrapper.in("tf_devicetype", "1","2","3");
        }else{
            queryWrapper.in("tf_devicetype", "4","5","6");
        }
        queryWrapper.like(StringUtils.isNotBlank(tfDevicename), "tf_devicename", tfDevicename);
        queryWrapper.like(StringUtils.isNotBlank(tfIpgateway), "tf_ipgateway", tfIpgateway);
        List<ScThermalImagingEntity> entities = baseDao.selectList(queryWrapper);
        List<Map<String, Object>> list =  new ArrayList<>();
        for (ScThermalImagingEntity entity : entities) {
            Map<String, Object> map = new HashMap<>();
            Object o = redisUtils.get("hotmapvalue:" + entity.getTfIpgateway());
            map.put("name",entity.getTfDevicename());
            map.put("ip",entity.getTfIpgateway());
            map.put("type",entity.getTfDevicetype());
            map.put("temperature",o);
            map.put("date", DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
            list.add(map);
        }
        return list;
    }

}
