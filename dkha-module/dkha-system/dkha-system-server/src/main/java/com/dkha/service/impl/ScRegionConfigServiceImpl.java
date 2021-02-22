package com.dkha.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.CloseableHttpClientToInterface;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScRegionConfigDao;
import com.dkha.dto.ScRegionConfigDTO;
import com.dkha.dto.UwbRegionDTO;
import com.dkha.entity.ScRegionConfigEntity;
import com.dkha.service.ScRegionConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 区域配置/uwb围栏关联
 *
 * @author Mark
 * @since v1.0.0 2020-09-01
 */
@Service
public class ScRegionConfigServiceImpl extends BaseServiceImpl<ScRegionConfigDao, ScRegionConfigEntity> implements ScRegionConfigService {

    @Value("${uwb.url}")
    private String uwbUrl;

    @Override
    public PageData<ScRegionConfigDTO> page(Map<String, Object> params) {
        paramsToLike(params,"rcName");
        IPage<ScRegionConfigEntity> page = baseDao.pageList(
                getPage(params, Constant.CREATE_DATE, false),params
        );
        return getPageData(page, ScRegionConfigDTO.class);
    }

    @Override
    public List<ScRegionConfigDTO> list(Map<String, Object> params) {
        List<ScRegionConfigEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScRegionConfigDTO.class);
    }

    private QueryWrapper<ScRegionConfigEntity> getWrapper(Map<String, Object> params){
        String rcName = (String)params.get("rcName");

        QueryWrapper<ScRegionConfigEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(rcName), "rc_name", rcName);
        return wrapper;
    }

    @Override
    public ScRegionConfigDTO get(String id) {
        ScRegionConfigEntity entity = baseDao.selectById(id);
        ScRegionConfigDTO scRegionConfigDTO = ConvertUtils.sourceToTarget(entity, ScRegionConfigDTO.class);
        scRegionConfigDTO.setRcFloor(entity.getRcFloor().split(","));
        return scRegionConfigDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScRegionConfigDTO dto) {
        QueryWrapper<ScRegionConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rc_no",dto.getRcNo());
        ScRegionConfigEntity scRegionConfigEntity = baseDao.selectOne(queryWrapper);
        if(scRegionConfigEntity != null){
            throw new RenException("区域编号已存在");
        }
        ScRegionConfigEntity entity = ConvertUtils.sourceToTarget(dto, ScRegionConfigEntity.class);
        entity.setRcFloor(String.join(",",dto.getRcFloor()));
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScRegionConfigDTO dto) {
        QueryWrapper<ScRegionConfigEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("rc_no",dto.getRcNo());
        ScRegionConfigEntity scRegionConfigEntity = baseDao.selectOne(queryWrapper);
        if(scRegionConfigEntity != null && !dto.getRcId().equals(scRegionConfigEntity.getRcId())){
            throw new RenException("区域编号已存在");
        }
        ScRegionConfigEntity entity = ConvertUtils.sourceToTarget(dto, ScRegionConfigEntity.class);
        entity.setRcFloor(String.join(",",dto.getRcFloor()));

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScRegionConfigEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<UwbRegionDTO> getUwbRegionList() {

        String str = "{\n" +
                "    \"status\": 200,\n" +
                "    \"result\": [\n" +
                "        {\n" +
                "            \"id\": 4,\n" +
                "            \"fenceName\": \"重要设备2\",\n" +
                "            \"mapId\": 12,\n" +
                "            \"floor\": \"F8\",\n" +
                "            \"coordinate\": \"[[4.85,9.89],[6.93,7.27],[9.83,8.87],[7.57,11.89]]\",\n" +
                "            \"adminId\": 1,\n" +
                "            \"updateTime\": \"2020-08-31 17:40:36\",\n" +
                "            \"createTime\": \"2020-08-31 17:40:36\",\n" +
                "            \"createUid\": 1,\n" +
                "            \"updateUid\": 1\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 5,\n" +
                "            \"fenceName\": \"保密区域1号\",\n" +
                "            \"mapId\": 12,\n" +
                "            \"floor\": \"F7\",\n" +
                "            \"coordinate\": \"[[-10.75,5.16],[-8.06,8.87],[-5.6,6.78],[-8.83,3.78]]\",\n" +
                "            \"adminId\": 1,\n" +
                "            \"updateTime\": \"2020-08-31 17:41:08\",\n" +
                "            \"createTime\": \"2020-08-31 17:41:08\",\n" +
                "            \"createUid\": 33,\n" +
                "            \"updateUid\": 33\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 6,\n" +
                "            \"fenceName\": \"访客禁止进入区域\",\n" +
                "            \"mapId\": 12,\n" +
                "            \"floor\": \"F7\",\n" +
                "            \"coordinate\": \"[[-7.93,2.91],[-5.16,6.23],[-4.4,5.08],[-7.01,2.04]]\",\n" +
                "            \"adminId\": 1,\n" +
                "            \"updateTime\": \"2020-08-31 18:20:09\",\n" +
                "            \"createTime\": \"2020-08-31 18:20:09\",\n" +
                "            \"createUid\": 33,\n" +
                "            \"updateUid\": 33\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        String s = CloseableHttpClientToInterface.doGet(uwbUrl + "/fence/fences");
        JSONObject jsonObject = JSONObject.parseObject(s);
        String status = jsonObject.getString("status");
        if ("200".equals(status)) {
            List<UwbRegionDTO> result = JSONArray.parseArray(jsonObject.getString("result"), UwbRegionDTO.class);
            return result;
        }
        return null;
    }

}
