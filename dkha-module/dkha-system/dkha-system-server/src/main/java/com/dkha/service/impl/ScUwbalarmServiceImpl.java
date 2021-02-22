package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScUwbalarmDao;
import com.dkha.dto.ScPmalarmDTO;
import com.dkha.dto.ScUwbalarmDTO;
import com.dkha.entity.ScAidooreightEntity;
import com.dkha.entity.ScPmalarmEntity;
import com.dkha.entity.ScUwbalarmEntity;
import com.dkha.service.ScUwbalarmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * uwb报警内容：工具标签报警，访客禁区报警，保密区域报警
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScUwbalarmServiceImpl extends BaseServiceImpl<ScUwbalarmDao, ScUwbalarmEntity> implements ScUwbalarmService {


    @Override
    public PageData<ScUwbalarmDTO> page(Map<String, Object> params) {
        // 转换成like
        paramsToLike(params, "ubaAddress");

        IPage<ScUwbalarmEntity> page = baseDao.getMyList( getPage(params, "", false), params);

        // 分页
//        int inowpage = Integer.parseInt((String) params.get("page"));
//        int ipagesize = Integer.parseInt((String) params.get("limit"));
//        int scol = ipagesize * (inowpage - 1);
//        params.put("scol", scol);
//        params.put("ipagesize", ipagesize);
//        // 查询
//        List<ScUwbalarmEntity> list = baseDao.getMyList(params);
//        long total = baseDao.getMyCount(params);

        return getPageData(page, ScUwbalarmDTO.class);
    }

    @Override
    public List<ScUwbalarmDTO> list(Map<String, Object> params) {
        List<ScUwbalarmEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScUwbalarmDTO.class);
    }

    private QueryWrapper<ScUwbalarmEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScUwbalarmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScUwbalarmDTO get(String id) {
        ScUwbalarmEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScUwbalarmDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScUwbalarmDTO dto) {
        ScUwbalarmEntity entity = ConvertUtils.sourceToTarget(dto, ScUwbalarmEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScUwbalarmDTO dto) {
        ScUwbalarmEntity entity = ConvertUtils.sourceToTarget(dto, ScUwbalarmEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScUwbalarmEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
