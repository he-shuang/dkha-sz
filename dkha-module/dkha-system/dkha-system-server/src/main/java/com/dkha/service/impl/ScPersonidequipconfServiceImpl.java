

package com.dkha.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.exception.RenException;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScPersonidequipDao;
import com.dkha.dao.ScPersonidequipconfDao;
import com.dkha.dto.ScPersonidequipDTO;
import com.dkha.dto.ScPersonidequipconfDTO;
import com.dkha.entity.ScPersonidequipEntity;
import com.dkha.entity.ScPersonidequipconfEntity;
import com.dkha.excel.ScPersonidequipImportExcel;
import com.dkha.excel.ScTransformerdcImportExcel;
import com.dkha.excel.listener.ScPersonidequipDataListener;
import com.dkha.excel.listener.ScTransformerdcDataListener;
import com.dkha.service.ScPersonidequipconfService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 人证配置信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Service
public class ScPersonidequipconfServiceImpl extends BaseServiceImpl<ScPersonidequipconfDao, ScPersonidequipconfEntity> implements ScPersonidequipconfService {

    @Autowired
    private ScPersonidequipDao scPersonidequipDao;

    @Override
    public PageData<ScPersonidequipconfDTO> page(Map<String, Object> params) {
        IPage<ScPersonidequipconfEntity> page = baseDao.selectPage(
                getPage(params, Constant.CREATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, ScPersonidequipconfDTO.class);
    }

    @Override
    public List<ScPersonidequipconfDTO> list(Map<String, Object> params) {
        List<ScPersonidequipconfEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, ScPersonidequipconfDTO.class);
    }

    private QueryWrapper<ScPersonidequipconfEntity> getWrapper(Map<String, Object> params){
        String id = (String)params.get("id");

        QueryWrapper<ScPersonidequipconfEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        //wrapper.eq(Constant.DEL_FLAG, DelFlagEnum.NORMAL.value());

        return wrapper;
    }

    @Override
    public ScPersonidequipconfDTO get(String id) {
        ScPersonidequipconfEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, ScPersonidequipconfDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(ScPersonidequipconfDTO dto) {
        ScPersonidequipconfEntity entity = ConvertUtils.sourceToTarget(dto, ScPersonidequipconfEntity.class);
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ScPersonidequipconfDTO dto) {
        ScPersonidequipconfEntity entity = ConvertUtils.sourceToTarget(dto, ScPersonidequipconfEntity.class);
        QueryWrapper<ScPersonidequipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pie_equipsn",dto.getPieEquipsn());
        ScPersonidequipEntity scPersonidequipEntity = scPersonidequipDao.selectOne(queryWrapper);
        scPersonidequipEntity.setPieIsinitial(0);
        scPersonidequipDao.updateById(scPersonidequipEntity);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {

        //物理删除
        baseDao.deleteById(id);
    }

    @Override
    public ScPersonidequipconfDTO getByEquipsn(String equipsn) {
        QueryWrapper<ScPersonidequipconfEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pie_equipsn",equipsn);
        ScPersonidequipconfEntity scPersonidequipconfEntity = baseDao.selectOne(queryWrapper);
        if (scPersonidequipconfEntity == null){
            scPersonidequipconfEntity = baseDao.getNewInfo();
        }
        return  ConvertUtils.sourceToTarget(baseDao.selectOne(queryWrapper), ScPersonidequipconfDTO.class);
    }

    @Override
    public void importExcel(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new RenException("上传文件为空");
        }
        try {
            EasyExcel.read(file.getInputStream(), ScPersonidequipImportExcel.class, new ScPersonidequipDataListener())
                    .sheet().headRowNumber(2).doRead();
        } catch (Exception e) {
            throw new RenException(e.getMessage());
        }
    }
}
