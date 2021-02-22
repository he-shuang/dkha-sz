package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScGateaccesscontrolDao;
import com.dkha.dto.ScGateaccesscontrolDTO;
import com.dkha.entity.ScGateaccesscontrolEntity;

import com.dkha.service.ScGateaccesscontrolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 门禁同行记录
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScGateaccesscontrolServiceImpl extends BaseServiceImpl<ScGateaccesscontrolDao, ScGateaccesscontrolEntity> implements ScGateaccesscontrolService {


    @Override
    public PageData<ScGateaccesscontrolDTO> page(Map<String, Object> params) {
        IPage<ScGateaccesscontrolEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScGateaccesscontrolDTO.class);
    }

    @Override
    public List<ScGateaccesscontrolDTO> list(Map<String, Object> params) {
        List<ScGateaccesscontrolEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScGateaccesscontrolDTO.class);
    }

    private QueryWrapper<ScGateaccesscontrolEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScGateaccesscontrolEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScGateaccesscontrolDTO get(String id) {
        ScGateaccesscontrolEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScGateaccesscontrolDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScGateaccesscontrolDTO dto) {
        ScGateaccesscontrolEntity entity = ConvertUtils.sourceToTarget(dto, ScGateaccesscontrolEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScGateaccesscontrolDTO dto) {
        ScGateaccesscontrolEntity entity = ConvertUtils.sourceToTarget(dto, ScGateaccesscontrolEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScGateaccesscontrolEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
