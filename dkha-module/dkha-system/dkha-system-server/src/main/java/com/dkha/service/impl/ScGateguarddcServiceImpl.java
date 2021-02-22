package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScGateguarddcDao;
import com.dkha.dto.ScGateguarddcDTO;
import com.dkha.entity.ScGateguarddcEntity;
import com.dkha.service.ScGateWayModBusDataChangeService;
import com.dkha.service.ScGateguarddcService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScGateguarddcServiceImpl extends BaseServiceImpl<ScGateguarddcDao, ScGateguarddcEntity> implements ScGateguarddcService {


    @Override
    public PageData<ScGateguarddcDTO> page(Map<String, Object> params) {
        IPage<ScGateguarddcEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScGateguarddcDTO.class);
    }

    @Override
    public List<ScGateguarddcDTO> list(Map<String, Object> params) {
        List<ScGateguarddcEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScGateguarddcDTO.class);
    }

    private QueryWrapper<ScGateguarddcEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScGateguarddcEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScGateguarddcDTO get(String id) {
        ScGateguarddcEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScGateguarddcDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScGateguarddcDTO dto) {
        ScGateguarddcEntity entity = ConvertUtils.sourceToTarget(dto, ScGateguarddcEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScGateguarddcDTO dto) {
        ScGateguarddcEntity entity = ConvertUtils.sourceToTarget(dto, ScGateguarddcEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScGateguarddcEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
