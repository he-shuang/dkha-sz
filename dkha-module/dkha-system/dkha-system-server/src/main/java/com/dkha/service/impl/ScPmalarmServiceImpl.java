package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScPmalarmDao;
import com.dkha.dto.ScPmalarmDTO;
import com.dkha.entity.ScPmalarmEntity;
import com.dkha.service.ScPmalarmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * PM2.5设备报警信息
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScPmalarmServiceImpl extends BaseServiceImpl<ScPmalarmDao, ScPmalarmEntity> implements ScPmalarmService {

    @Override
    public PageData<ScPmalarmDTO> page(Map<String, Object> params) {
        // 转换成like
        paramsToLike(params, "pmaAddress","devicename");

        // 分页
        int inowpage = Integer.parseInt((String) params.get("page"));
        int ipagesize = Integer.parseInt((String) params.get("limit"));
        int scol = ipagesize * (inowpage - 1);
        params.put("scol", scol);
        params.put("ipagesize", ipagesize);
        // 查询
        List<ScPmalarmEntity> list = baseDao.getMyList(params);
        long total = baseDao.getMyCount(params);

        return getPageData(list, total, ScPmalarmDTO.class);
    }

    @Override
    public List<ScPmalarmDTO> list(Map<String, Object> params) {
        List<ScPmalarmEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScPmalarmDTO.class);
    }

    private QueryWrapper<ScPmalarmEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScPmalarmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScPmalarmDTO get(String id) {
        ScPmalarmEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScPmalarmDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScPmalarmDTO dto) {
        ScPmalarmEntity entity = ConvertUtils.sourceToTarget(dto, ScPmalarmEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScPmalarmDTO dto) {
        ScPmalarmEntity entity = ConvertUtils.sourceToTarget(dto, ScPmalarmEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScPmalarmEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
