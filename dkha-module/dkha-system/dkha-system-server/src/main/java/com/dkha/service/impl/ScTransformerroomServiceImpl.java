package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScTransformerroomDao;
import com.dkha.dto.ScTransformerroomDTO;
import com.dkha.entity.ScTransformerroomEntity;
import com.dkha.service.ScTransformerroomService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 互感器宿舍关联关系
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScTransformerroomServiceImpl extends BaseServiceImpl<ScTransformerroomDao, ScTransformerroomEntity> implements ScTransformerroomService {


    @Override
    public PageData<ScTransformerroomDTO> page(Map<String, Object> params) {
        IPage<ScTransformerroomEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScTransformerroomDTO.class);
    }

    @Override
    public List<ScTransformerroomDTO> list(Map<String, Object> params) {
        List<ScTransformerroomEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScTransformerroomDTO.class);
    }

    private QueryWrapper<ScTransformerroomEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScTransformerroomEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScTransformerroomDTO get(String id) {
        ScTransformerroomEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScTransformerroomDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScTransformerroomDTO dto) {
        ScTransformerroomEntity entity = ConvertUtils.sourceToTarget(dto, ScTransformerroomEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScTransformerroomDTO dto) {
        ScTransformerroomEntity entity = ConvertUtils.sourceToTarget(dto, ScTransformerroomEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScTransformerroomEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void deleteByGateId(String tfId) {
        baseDao.deleteByGateId(tfId);
    }

    @Override
    public List<ScTransformerroomDTO> findAllById(String id) {
        return baseDao.findAllById(id);
    }

    @Override
    public ScTransformerroomEntity selectRoomById(Long drId) {
        return baseDao.selectRoomById(drId);
    }

    @Override
    public void deleteByNull() {
        baseDao.deleteByNull();
    }

    @Override
    public List<ScTransformerroomEntity> selectByTfId(Long tfId) {
        return baseDao.selectByTfId(tfId);
    }

}
