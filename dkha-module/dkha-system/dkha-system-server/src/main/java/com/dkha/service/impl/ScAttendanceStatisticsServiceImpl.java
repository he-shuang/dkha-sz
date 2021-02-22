package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScAttendanceStatisticsDao;
import com.dkha.dto.ScAttendanceStatisticsDTO;
import com.dkha.dto.ScAttendanceStatisticsDataDTO;
import com.dkha.entity.ScAttendanceStatisticsEntity;
import com.dkha.service.ScAttendanceStatisticsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 考勤统计
 *
 * @author Mark 
 * @since v1.0.0 2020-12-14
 */
@Service
public class ScAttendanceStatisticsServiceImpl extends BaseServiceImpl<ScAttendanceStatisticsDao, ScAttendanceStatisticsEntity> implements ScAttendanceStatisticsService {


    @Override
    public PageData<ScAttendanceStatisticsDTO> page(Map<String, Object> params) {
        IPage<ScAttendanceStatisticsEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScAttendanceStatisticsDTO.class);
    }

    @Override
    public List<ScAttendanceStatisticsDTO> list(Map<String, Object> params) {
        List<ScAttendanceStatisticsEntity> entityList = baseDao.list(params);

        return ConvertUtils.sourceToTarget(entityList, ScAttendanceStatisticsDTO.class);
    }

    private QueryWrapper<ScAttendanceStatisticsEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScAttendanceStatisticsEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScAttendanceStatisticsDTO get(String id) {
        ScAttendanceStatisticsEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScAttendanceStatisticsDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScAttendanceStatisticsDTO dto) {
        ScAttendanceStatisticsEntity entity = ConvertUtils.sourceToTarget(dto, ScAttendanceStatisticsEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScAttendanceStatisticsDTO dto) {
        ScAttendanceStatisticsEntity entity = ConvertUtils.sourceToTarget(dto, ScAttendanceStatisticsEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, ScAttendanceStatisticsEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public List<ScAttendanceStatisticsDataDTO> dataInfo(Map<String, Object> params) {
        paramsToLike(params,"scWaname");
        List<ScAttendanceStatisticsDataDTO> list = baseDao.dataInfo(params);

        return list;
    }

}