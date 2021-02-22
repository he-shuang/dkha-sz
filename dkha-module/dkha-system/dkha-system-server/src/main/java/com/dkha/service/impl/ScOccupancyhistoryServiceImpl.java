package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScOccupancyhistoryDao;
import com.dkha.dto.ScOccupancyhistoryDTO;
import com.dkha.entity.ScOccupancyhistoryEntity;
import com.dkha.service.ScOccupancyhistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 某房间的入住历史记录
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScOccupancyhistoryServiceImpl extends BaseServiceImpl<ScOccupancyhistoryDao, ScOccupancyhistoryEntity> implements ScOccupancyhistoryService {


    @Override
    public PageData<ScOccupancyhistoryDTO> page(Map<String, Object> params) {
        IPage<ScOccupancyhistoryEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScOccupancyhistoryDTO.class);
    }

    @Override
    public List<ScOccupancyhistoryDTO> list(Map<String, Object> params) {
        List<ScOccupancyhistoryEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScOccupancyhistoryDTO.class);
    }

    private QueryWrapper<ScOccupancyhistoryEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScOccupancyhistoryEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScOccupancyhistoryDTO get(String id) {
        ScOccupancyhistoryEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScOccupancyhistoryDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScOccupancyhistoryDTO dto) {
        ScOccupancyhistoryEntity entity = ConvertUtils.sourceToTarget(dto, ScOccupancyhistoryEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScOccupancyhistoryDTO dto) {
        ScOccupancyhistoryEntity entity = ConvertUtils.sourceToTarget(dto, ScOccupancyhistoryEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScOccupancyhistoryEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
