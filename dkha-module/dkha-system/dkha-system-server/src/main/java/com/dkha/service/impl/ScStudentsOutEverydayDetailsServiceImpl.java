package com.dkha.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
import com.dkha.commons.tools.constant.Constant;
import com.dkha.commons.tools.page.PageData;
import com.dkha.commons.tools.utils.ConvertUtils;
import com.dkha.dao.ScStudentsOutEverydayDetailsDao;
import com.dkha.dto.ScStudentsCountDTO;
import com.dkha.dto.ScStudentsOutEverydayDetailsDTO;
import com.dkha.entity.ScAidooreightversionEntity;
import com.dkha.entity.ScStudentsOutEverydayDetailsEntity;
import com.dkha.service.ScStudentsOutEverydayDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 每日学生未归详情
 *
 * @author Mark 
 * @since v1.0.0 2020-10-15
 */
@Service
public class ScStudentsOutEverydayDetailsServiceImpl extends BaseServiceImpl<ScStudentsOutEverydayDetailsDao, ScStudentsOutEverydayDetailsEntity> implements ScStudentsOutEverydayDetailsService {


    @Override
    public PageData<ScStudentsOutEverydayDetailsDTO> page(Map<String, Object> params) {
        IPage<ScStudentsOutEverydayDetailsDTO> page = baseDao.findPage(
                getPage(params, "create_date", false),
                params
        );

        return getPageData(page, ScStudentsOutEverydayDetailsDTO.class);
    }

    @Override
    public PageData<ScStudentsCountDTO> findCountSum(Map<String, Object> params) {
        IPage<ScStudentsCountDTO> page = baseDao.findCountSum(getPage(params, "create_date", false),
                params
        );
        return getPageData(page, ScStudentsCountDTO.class);
    }

    @Override
    public PageData<ScStudentsCountDTO> findCountRoomSum(Map<String, Object> params) {
        IPage<ScStudentsCountDTO> page = baseDao.findCountRoomSum(getPage(params, "create_date", false),
                params
        );
        return getPageData(page, ScStudentsCountDTO.class);
    }

    @Override
    public List<ScStudentsCountDTO> findList(Map<String, Object> params) {
        return baseDao.findList(params);
    }

    @Override
    public List<ScStudentsCountDTO> findCountRoomExcel(Map<String, Object> params) {
        return baseDao.findCountRoomExcel(params);
    }

}