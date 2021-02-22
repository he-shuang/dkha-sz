package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.enums.DelFlagEnum;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScAidoorfivePersonlistDao;
import com.dkha.dto.ScAidoorfivePersonlistDTO;
import com.dkha.entity.ScAidoorfivePersonlistEntity;
import com.dkha.service.ScAidoorfivePersonlistService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-16
 */
@Service
public class ScAidoorfivePersonlistServiceImpl extends BaseServiceImpl<ScAidoorfivePersonlistDao, ScAidoorfivePersonlistEntity> implements ScAidoorfivePersonlistService {


    @Override
    public PageData<ScAidoorfivePersonlistDTO> page(Map<String, Object> params) {
        IPage<ScAidoorfivePersonlistEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScAidoorfivePersonlistDTO.class);
    }

    @Override
    public List<ScAidoorfivePersonlistDTO> list(Map<String, Object> params) {
        List<ScAidoorfivePersonlistEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScAidoorfivePersonlistDTO.class);
    }

    private QueryWrapper<ScAidoorfivePersonlistEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScAidoorfivePersonlistEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScAidoorfivePersonlistDTO get(String id) {
        ScAidoorfivePersonlistEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScAidoorfivePersonlistDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScAidoorfivePersonlistDTO dto) {
        ScAidoorfivePersonlistEntity entity = ConvertUtils.sourceToTarget(dto, ScAidoorfivePersonlistEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScAidoorfivePersonlistDTO dto) {
        ScAidoorfivePersonlistEntity entity = ConvertUtils.sourceToTarget(dto, ScAidoorfivePersonlistEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScAidoorfivePersonlistEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}