//package com.dkha.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import com.dkha.commons.dynamic.datasource.annotation.DataSource;
//import com.dkha.commons.mybatis.enums.DelFlagEnum;
//import com.dkha.commons.dynamic.datasource.annotation.DataSource;
//import com.dkha.commons.mybatis.service.impl.BaseServiceImpl;
//import com.dkha.commons.tools.page.PageData;
//import com.dkha.commons.tools.utils.ConvertUtils;
//import com.dkha.dao.FvScDoorRecordDao;
//import com.dkha.dto.FvScDoorRecordDTO;
//import com.dkha.entity.FvScDoorRecordEntity;
//import com.dkha.service.FvScDoorRecordService;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
///**
// * 开门记录表
// *
// * @author linhc linhc@dkay-cn.com
// * @since v1.0.0 2020-09-25
// */
//@Service
////5寸门禁多数据源配置 深圳使用
////@DataSource("slave2")
//public class FvScDoorRecordServiceImpl extends BaseServiceImpl<FvScDoorRecordDao, FvScDoorRecordEntity> implements FvScDoorRecordService {
//    @Override
//    public PageData<FvScDoorRecordDTO> page(Map<String, Object> params) {
//        // 转换成like
//        paramsToLike(params, "fName");
//        paramsToLike(params, "fserialNumber");
//        paramsToLike(params, "deviceName");
//        // 分页
//        int inowpage = Integer.parseInt((String) params.get("page"));
//        int ipagesize = Integer.parseInt((String) params.get("limit"));
//        int scol = ipagesize * (inowpage - 1);
//        params.put("scol", scol);
//        params.put("ipagesize", ipagesize);
//        // 查询
//        List<FvScDoorRecordEntity> list = baseDao.getMyList(params);
//        for(FvScDoorRecordEntity entity : list){
//            entity.setFAddDate(new Date(entity.getFAddTime() * 1000L));
//        }
//        long total = baseDao.getMyCount(params);
//
//        return getPageData(list, total, FvScDoorRecordDTO.class);
//    }
//
//    @Override
//    public FvScDoorRecordDTO getMyOne(String fId) {
//        FvScDoorRecordEntity entity = baseDao.getMyOne(fId);
//        entity.setFAddDate(new Date(entity.getFAddTime() * 1000L));
//        return ConvertUtils.sourceToTarget(entity, FvScDoorRecordDTO.class);
//    }
//
//}