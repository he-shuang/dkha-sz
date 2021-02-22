package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScDormitoryDao;
import com.dkha.dto.ScDormitoryDTO;
import com.dkha.entity.ScDormitoryEntity;
import com.dkha.service.ScDormitoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 房间及房间状态信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScDormitoryServiceImpl extends BaseServiceImpl<ScDormitoryDao, ScDormitoryEntity> implements ScDormitoryService {


    @Override
    public PageData<ScDormitoryDTO> page(Map<String, Object> params) {
        IPage<ScDormitoryEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScDormitoryDTO.class);
    }

    @Override
    public List<ScDormitoryDTO> list(Map<String, Object> params) {
        List<ScDormitoryEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScDormitoryDTO.class);
    }

    private QueryWrapper<ScDormitoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScDormitoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScDormitoryDTO get(String id) {
        ScDormitoryEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScDormitoryDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScDormitoryDTO dto) {
        ScDormitoryEntity entity = ConvertUtils.sourceToTarget(dto, ScDormitoryEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScDormitoryDTO dto) {
        ScDormitoryEntity entity = ConvertUtils.sourceToTarget(dto, ScDormitoryEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScDormitoryEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
