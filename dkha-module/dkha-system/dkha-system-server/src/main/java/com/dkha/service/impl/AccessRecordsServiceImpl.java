package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.AccessRecordsDao;
import com.dkha.dto.AccessRecordsDTO;
import com.dkha.entity.AccessRecordsEntity;
import com.dkha.service.AccessRecordsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 出入记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-30
 */
@Service
public class AccessRecordsServiceImpl extends BaseServiceImpl<AccessRecordsDao, AccessRecordsEntity> implements AccessRecordsService {


    @Override
    public PageData<AccessRecordsDTO> page(Map<String, Object> params) {
        IPage<AccessRecordsEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, AccessRecordsDTO.class);
    }

    @Override
    public List<AccessRecordsDTO> list(Map<String, Object> params) {
        List<AccessRecordsEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, AccessRecordsDTO.class);
    }

    private QueryWrapper<AccessRecordsEntity> getWrapper(Map<String, Object> params){
        String studentNum = (String)params.get("studentNum");
        String name = (String)params.get("name");
        String status = (String)params.get("status");

        QueryWrapper<AccessRecordsEntity> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isNotBlank(studentNum), "student_num", studentNum);
        wrapper.like(StringUtils.isNotBlank(name), "name", name);
        wrapper.eq(StringUtils.isNotBlank(status), "status", status);

        return wrapper;
    }

    @Override
    public AccessRecordsDTO get(String id) {
        AccessRecordsEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, AccessRecordsDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(AccessRecordsDTO dto) {
        AccessRecordsEntity entity = ConvertUtils.sourceToTarget(dto, AccessRecordsEntity.class);

        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(AccessRecordsDTO dto) {
        AccessRecordsEntity entity = ConvertUtils.sourceToTarget(dto, AccessRecordsEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String[] ids) {
        //逻辑删除
        //logicDelete(ids, AccessRecordsEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

}
