package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScConsumptionsystemVipDao;
import com.dkha.dto.ScConsumptionsystemVipDTO;
import com.dkha.entity.ScConsumptionsystemVipEntity;
import com.dkha.service.ScConsumptionsystemVipService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 消费系统会员信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Service
public class ScConsumptionsystemVipServiceImpl extends BaseServiceImpl<ScConsumptionsystemVipDao, ScConsumptionsystemVipEntity> implements ScConsumptionsystemVipService {


    @Override
    public PageData<ScConsumptionsystemVipDTO> page(Map<String, Object> params) {
        IPage<ScConsumptionsystemVipEntity> page = baseDao.selectPage(
                getPage(params, "register_date", false),
                getWrapper(params)
        );

        return getPageData(page, ScConsumptionsystemVipDTO.class);
    }

    @Override
    public List<ScConsumptionsystemVipDTO> list(Map<String, Object> params) {
        List<ScConsumptionsystemVipEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScConsumptionsystemVipDTO.class);
    }

    private QueryWrapper<ScConsumptionsystemVipEntity> getWrapper(Map<String, Object> params){
        String cardId = (String)params.get("cardId");
        String name = (String)params.get("name");

        QueryWrapper<ScConsumptionsystemVipEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(name), "name", name);
        wrapper.like(StringUtils.isNotBlank(cardId), "card_id", cardId);

        return wrapper;
    }

    @Override
    public ScConsumptionsystemVipDTO get(String id) {
        ScConsumptionsystemVipEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScConsumptionsystemVipDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScConsumptionsystemVipDTO dto) {
        ScConsumptionsystemVipEntity entity = ConvertUtils.sourceToTarget(dto, ScConsumptionsystemVipEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScConsumptionsystemVipDTO dto) {
        ScConsumptionsystemVipEntity entity = ConvertUtils.sourceToTarget(dto, ScConsumptionsystemVipEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScConsumptionsystemVipEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}