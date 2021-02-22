package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScTransformalarmDao;
import com.dkha.dto.ScTransformalarmDTO;
import com.dkha.entity.ScTransformalarmEntity;
import com.dkha.service.ScTransformalarmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 电流互感器房间电流信息报警
 *
 * @since v1.0.0 2020-08-23
 */
@Service
public class ScTransformalarmServiceImpl extends BaseServiceImpl<ScTransformalarmDao, ScTransformalarmEntity> implements ScTransformalarmService {


    @Override
    public PageData<ScTransformalarmDTO> page(Map<String, Object> params) {
        // 分页
        int inowpage = Integer.parseInt((String) params.get("page"));
        int ipagesize = Integer.parseInt((String) params.get("limit"));
        int scol = ipagesize * (inowpage - 1);
        params.put("scol", scol);
        params.put("ipagesize", ipagesize);
        // 查询
        List<ScTransformalarmEntity> list = baseDao.getMyList(params);
        long total = baseDao.getMyCount(params);

        return getPageData(list, total, ScTransformalarmDTO.class);
    }

    @Override
    public List<ScTransformalarmDTO> list(Map<String, Object> params) {
        List<ScTransformalarmEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScTransformalarmDTO.class);
    }

    private QueryWrapper<ScTransformalarmEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScTransformalarmEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScTransformalarmDTO get(String id) {
        ScTransformalarmEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScTransformalarmDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScTransformalarmDTO dto) {
        ScTransformalarmEntity entity = ConvertUtils.sourceToTarget(dto, ScTransformalarmEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScTransformalarmDTO dto) {
        ScTransformalarmEntity entity = ConvertUtils.sourceToTarget(dto, ScTransformalarmEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScTransformalarmEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
