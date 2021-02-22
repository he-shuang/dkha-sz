package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScStudentsInandoutDao;
import com.dkha.dto.ScRoomCountDTO;
import com.dkha.dto.ScStudentsInandoutDTO;
import com.dkha.entity.ScStudentsInandoutEntity;
import com.dkha.service.ScStudentsInandoutService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author dkha 
 * @since v1.0.0 2020-10-22
 */
@Service
public class ScStudentsInandoutServiceImpl extends BaseServiceImpl<ScStudentsInandoutDao, ScStudentsInandoutEntity> implements ScStudentsInandoutService {


    @Override
    public PageData<ScStudentsInandoutDTO> page(Map<String, Object> params) {
        IPage<ScStudentsInandoutEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScStudentsInandoutDTO.class);
    }

    @Override
    public List<ScStudentsInandoutDTO> list(Map<String, Object> params) {
        List<ScStudentsInandoutEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScStudentsInandoutDTO.class);
    }

    private QueryWrapper<ScStudentsInandoutEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScStudentsInandoutEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScStudentsInandoutDTO get(String id) {
        ScStudentsInandoutEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScStudentsInandoutDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScStudentsInandoutDTO dto) {
        ScStudentsInandoutEntity entity = ConvertUtils.sourceToTarget(dto, ScStudentsInandoutEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScStudentsInandoutDTO dto) {
        ScStudentsInandoutEntity entity = ConvertUtils.sourceToTarget(dto, ScStudentsInandoutEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScStudentsInandoutEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}