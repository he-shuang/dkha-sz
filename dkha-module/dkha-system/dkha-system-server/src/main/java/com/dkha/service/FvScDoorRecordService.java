//package com.dkha.service;
//
//import com.dkha.commons.mybatis.service.BaseService;
//import com.dkha.commons.tools.page.PageData;
//import com.dkha.dto.FvScDoorRecordDTO;
//import com.dkha.entity.FvScDoorRecordEntity;
//
//import java.util.List;
//import java.util.Map;
//
///**
// * 开门记录表
// *
// * @author linhc linhc@dkay-cn.com
// * @since v1.0.0 2020-09-25
// */
//public interface FvScDoorRecordService extends BaseService<FvScDoorRecordEntity> {
//
//    PageData<FvScDoorRecordDTO> page(Map<String, Object> params);
//
//    /**
//     * 通过设备ID获取设备信息
//     * @param fId
//     * @return
//     */
//    FvScDoorRecordDTO getMyOne(String fId);
//}