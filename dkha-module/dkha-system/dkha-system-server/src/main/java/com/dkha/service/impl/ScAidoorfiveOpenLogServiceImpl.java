package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScAidoorfiveOpenLogDao;
import com.dkha.dto.ScAidoorfiveOpenLogDTO;
import com.dkha.entity.ScAidoorfiveOpenLogEntity;
import com.dkha.entity.ScFaceverificationEntity;
import com.dkha.service.ScAidoorfiveOpenLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-29
 */
@Service
public class ScAidoorfiveOpenLogServiceImpl extends BaseServiceImpl<ScAidoorfiveOpenLogDao, ScAidoorfiveOpenLogEntity> implements ScAidoorfiveOpenLogService {

    @Override
    public PageData<ScAidoorfiveOpenLogDTO> page(Map<String, Object> params) {
        paramsToLike(params,"name");
        paramsToLike(params,"deviceName");
        paramsToLike(params,"serialNumber");
        //获取分页数据
        IPage<ScAidoorfiveOpenLogEntity> page = baseDao.findPage(getPage(params, "add_date", false),
               params);

        return getPageData(page, ScAidoorfiveOpenLogDTO.class);
    }

    protected Map<String, Object> paramsToLike(Map<String, Object> params, String... likes){
        for (String like : likes){
            String val = (String)params.get(like);
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotBlank(val)){
                params.put(like, "%" + val + "%");
            }else {
                params.put(like, null);
            }
        }
        return params;
    }

    @Override
    public List<ScAidoorfiveOpenLogDTO> list(Map<String, Object> params) {
        List<ScAidoorfiveOpenLogEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScAidoorfiveOpenLogDTO.class);
    }

    private QueryWrapper<ScAidoorfiveOpenLogEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScAidoorfiveOpenLogEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScAidoorfiveOpenLogDTO get(String id) {
        ScAidoorfiveOpenLogEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScAidoorfiveOpenLogDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScAidoorfiveOpenLogDTO dto) {
        ScAidoorfiveOpenLogEntity entity = ConvertUtils.sourceToTarget(dto, ScAidoorfiveOpenLogEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScAidoorfiveOpenLogDTO dto) {
        ScAidoorfiveOpenLogEntity entity = ConvertUtils.sourceToTarget(dto, ScAidoorfiveOpenLogEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScAidoorfiveOpenLogEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}