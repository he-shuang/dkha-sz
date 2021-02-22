package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScAidooreightPersonlistDao;
import com.dkha.dto.ScAidooreightPersonlistDTO;
import com.dkha.entity.ScAidooreightEntity;
import com.dkha.entity.ScAidooreightPersonlistEntity;
import com.dkha.exception.ModuleErrorCode;
import com.dkha.service.ScAidooreightPersonlistService;
import com.dkha.service.ScAidooreightService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 8英寸智能门禁设备具体的人脸信息
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-16
 */
@Service
public class ScAidooreightPersonlistServiceImpl extends BaseServiceImpl<ScAidooreightPersonlistDao, ScAidooreightPersonlistEntity> implements ScAidooreightPersonlistService {

    @Autowired
    private ScAidooreightService scAidooreightService;


    @Override
    public PageData<ScAidooreightPersonlistDTO> page(Map<String, Object> params) {
        IPage<ScAidooreightPersonlistEntity> page = baseDao.selectPage(
                getPage(params, "update_date", false),
                getWrapper(params)
        );

        return getPageData(page, ScAidooreightPersonlistDTO.class);
    }

    @Override
    public List<ScAidooreightPersonlistDTO> list(Map<String, Object> params) {
        List<ScAidooreightPersonlistEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScAidooreightPersonlistDTO.class);
    }

    private QueryWrapper<ScAidooreightPersonlistEntity> getWrapper(Map<String, Object> params){
        String aeId = (String)params.get("aeId");
        String username = (String)params.get("username");

        QueryWrapper<ScAidooreightPersonlistEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(aeId), "ae_id", aeId);
        wrapper.like(StringUtils.isNotBlank(username), "username", username);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScAidooreightPersonlistDTO get(String id) {
        ScAidooreightPersonlistEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScAidooreightPersonlistDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScAidooreightPersonlistDTO dto) {
        ScAidooreightPersonlistEntity entity = ConvertUtils.sourceToTarget(dto, ScAidooreightPersonlistEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScAidooreightPersonlistDTO dto) {
        ScAidooreightPersonlistEntity entity = ConvertUtils.sourceToTarget(dto, ScAidooreightPersonlistEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {

        List<ScAidooreightPersonlistEntity> scAidooreightPersonlistEntities = baseDao.selectBatchIds(Arrays.asList(ids));
        for (ScAidooreightPersonlistEntity scAidooreightPersonlistEntity : scAidooreightPersonlistEntities) {
            if(scAidooreightPersonlistEntity != null){
                boolean b = scAidooreightService.deletePersonFromDeviceBySerial(scAidooreightPersonlistEntity.getAeId().toString(), scAidooreightPersonlistEntity.getUserid().toString());
                if (b){
                    //物理删除
                    baseDao.deleteById(scAidooreightPersonlistEntity.getApId());

                    //修改数量
                    ScAidooreightEntity scAidooreightEntity = scAidooreightService.selectById(scAidooreightPersonlistEntity.getAeId());
                    scAidooreightEntity.setAeFacetotal(scAidooreightEntity.getAeFacetotal() - 1);
                    scAidooreightService.updateById(scAidooreightEntity);
                }
            }
        }

    }

    @Override
    public void insertBatchAndUpdate(List<ScAidooreightPersonlistDTO> personlistDTOS) {
        List<ScAidooreightPersonlistEntity> scAidooreightPersonlistEntities = ConvertUtils.sourceToTarget(personlistDTOS, ScAidooreightPersonlistEntity.class);
        baseDao.insertBatchAndUpdate(scAidooreightPersonlistEntities);
    }

    @Override
    public void batchDelete(Map<String, String[]> params) {
//        String[] userids, String[] aeids
        String[] userids = params.get("userids");
        String[] aeids = params.get("aeids");
        for (String aeid : aeids) {
            for (String userid : userids) {
                boolean b = scAidooreightService.deletePersonFromDeviceBySerial(aeid, userid);
                if (b){
                    //物理删除
                    baseDao.delete(new QueryWrapper<ScAidooreightPersonlistEntity>().eq("ae_id",aeid).eq("userid",userid));

                    //修改数量
                    ScAidooreightEntity scAidooreightEntity = scAidooreightService.selectById(aeid);
                    scAidooreightEntity.setAeFacetotal(scAidooreightEntity.getAeFacetotal() - 1);
                    scAidooreightService.updateById(scAidooreightEntity);
                }
            }
        }
    }

    @Override
    public boolean delAllByUser(String userNo) {
        QueryWrapper<ScAidooreightPersonlistEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userno",userNo);
        List<ScAidooreightPersonlistEntity> perList = baseDao.selectList(wrapper);
        if (perList.isEmpty()){
            return false;
        }
        List<String> ids = new ArrayList();
        for (ScAidooreightPersonlistEntity personlistEntity : perList){
            ids.add(personlistEntity.getApId().toString());
        }
        String[] longs = ids.toArray(new String[ids.size()]);

        delete(longs);
        return true;
    }

}