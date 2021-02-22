package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScConsumptionsystemConsumeDao;
import com.dkha.dto.ScConsumptionsystemConsumeDTO;
import com.dkha.entity.ScConsumptionsystemConsumeEntity;
import com.dkha.entity.ScConsumptionsystemRechargeEntity;
import com.dkha.service.ScConsumptionsystemConsumeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 消费系统的消费记录
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Service
public class ScConsumptionsystemConsumeServiceImpl extends BaseServiceImpl<ScConsumptionsystemConsumeDao, ScConsumptionsystemConsumeEntity> implements ScConsumptionsystemConsumeService {


    @Override
    public PageData<ScConsumptionsystemConsumeDTO> page(Map<String, Object> params) {
        paramsToLike(params,"name","cardId");
        IPage<ScConsumptionsystemConsumeEntity> page = baseDao.findPage(
                getPage(params, Constant.CREATE_DATE, false),params
        );

        return getPageData(page, ScConsumptionsystemConsumeDTO.class);
    }

    @Override
    public List<ScConsumptionsystemConsumeDTO> list(Map<String, Object> params) {
        List<ScConsumptionsystemConsumeEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScConsumptionsystemConsumeDTO.class);
    }

    private QueryWrapper<ScConsumptionsystemConsumeEntity> getWrapper(Map<String, Object> params){

        String cardId = (String)params.get("cardId");

        QueryWrapper<ScConsumptionsystemConsumeEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(cardId), "card_id", cardId);

        return wrapper;
    }

    @Override
    public ScConsumptionsystemConsumeDTO get(String id) {
        ScConsumptionsystemConsumeEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScConsumptionsystemConsumeDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScConsumptionsystemConsumeDTO dto) {
        ScConsumptionsystemConsumeEntity entity = ConvertUtils.sourceToTarget(dto, ScConsumptionsystemConsumeEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScConsumptionsystemConsumeDTO dto) {
        ScConsumptionsystemConsumeEntity entity = ConvertUtils.sourceToTarget(dto, ScConsumptionsystemConsumeEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScConsumptionsystemConsumeEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}