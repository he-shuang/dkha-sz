package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScCurrenthistorySumDao;
import com.dkha.dto.ScCurrenthistorySumDTO;
import com.dkha.entity.ScCurrenthistorySumEntity;
import com.dkha.service.ScCurrenthistorySumService;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-11-06
 */
@Service
public class ScCurrenthistorySumServiceImpl extends BaseServiceImpl<ScCurrenthistorySumDao, ScCurrenthistorySumEntity> implements ScCurrenthistorySumService {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public PageData<ScCurrenthistorySumDTO> page(Map<String, Object> params) {
        IPage<ScCurrenthistorySumEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScCurrenthistorySumDTO.class);
    }

    @Override
    public List<ScCurrenthistorySumDTO> list(Map<String, Object> params) {
        List<ScCurrenthistorySumEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScCurrenthistorySumDTO.class);
    }

    private QueryWrapper<ScCurrenthistorySumEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScCurrenthistorySumEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScCurrenthistorySumDTO get(String id) {
        ScCurrenthistorySumEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScCurrenthistorySumDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScCurrenthistorySumDTO dto) {
        ScCurrenthistorySumEntity entity = ConvertUtils.sourceToTarget(dto, ScCurrenthistorySumEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScCurrenthistorySumDTO dto) {
        ScCurrenthistorySumEntity entity = ConvertUtils.sourceToTarget(dto, ScCurrenthistorySumEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScCurrenthistorySumEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<ScCurrenthistorySumEntity> getByTop() {
        return baseDao.getByTop( sdf.format(new Date()));
    }

}