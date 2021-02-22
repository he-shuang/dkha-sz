package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScConsumptionsystemRechargeDao;
import com.dkha.dto.ScConsumptionsystemRechargeDTO;
import com.dkha.entity.ScConsumptionsystemRechargeEntity;
import com.dkha.service.ScConsumptionsystemRechargeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 消费系统充值信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Service
public class ScConsumptionsystemRechargeServiceImpl extends BaseServiceImpl<ScConsumptionsystemRechargeDao, ScConsumptionsystemRechargeEntity> implements ScConsumptionsystemRechargeService {


    @Override
    public PageData<ScConsumptionsystemRechargeDTO> page(Map<String, Object> params) {
        paramsToLike(params,"name","cardId");
        IPage<ScConsumptionsystemRechargeEntity> page = baseDao.findPage(
                getPage(params, Constant.CREATE_DATE, false),params
        );

        return getPageData(page, ScConsumptionsystemRechargeDTO.class);
    }

    @Override
    public List<ScConsumptionsystemRechargeDTO> list(Map<String, Object> params) {
        List<ScConsumptionsystemRechargeEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScConsumptionsystemRechargeDTO.class);
    }

    private QueryWrapper<ScConsumptionsystemRechargeEntity> getWrapper(Map<String, Object> params){
        String cardId = (String)params.get("cardId");

        QueryWrapper<ScConsumptionsystemRechargeEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(cardId), "card_id", cardId);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScConsumptionsystemRechargeDTO get(String id) {
        ScConsumptionsystemRechargeEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScConsumptionsystemRechargeDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScConsumptionsystemRechargeDTO dto) {
        ScConsumptionsystemRechargeEntity entity = ConvertUtils.sourceToTarget(dto, ScConsumptionsystemRechargeEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScConsumptionsystemRechargeDTO dto) {
        ScConsumptionsystemRechargeEntity entity = ConvertUtils.sourceToTarget(dto, ScConsumptionsystemRechargeEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScConsumptionsystemRechargeEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}