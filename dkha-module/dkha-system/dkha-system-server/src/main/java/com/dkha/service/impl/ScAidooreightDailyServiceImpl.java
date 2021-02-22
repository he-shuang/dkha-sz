package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.enums.DelFlagEnum;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScAidooreightDailyDao;
import com.dkha.dto.ScAidooreightDailyDTO;
import com.dkha.entity.ScAidooreightDailyEntity;
import com.dkha.exception.ModuleErrorCode;
import com.dkha.service.ScAidooreightDailyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 8英寸智能门禁设备每日采集数量
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@Service
public class ScAidooreightDailyServiceImpl extends BaseServiceImpl<ScAidooreightDailyDao, ScAidooreightDailyEntity> implements ScAidooreightDailyService {
    @Override
    public PageData<ScAidooreightDailyDTO> page(Map<String, Object> params) {
        IPage<ScAidooreightDailyEntity> page = baseDao.selectPage(
                getPage(params, "md_gatherdate", false),
                getWrapper(params)
        );

        return getPageData(page, ScAidooreightDailyDTO.class);
    }

    @Override
    public List<ScAidooreightDailyDTO> list(Map<String, Object> params) {
        List<ScAidooreightDailyEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScAidooreightDailyDTO.class);
    }

    private QueryWrapper<ScAidooreightDailyEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScAidooreightDailyEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScAidooreightDailyDTO get(String id) {
        ScAidooreightDailyEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScAidooreightDailyDTO.class);
    }
}