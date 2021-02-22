//package com.dkha.dao;
//
//import com.dkha.commons.mybatis.dao.BaseDao;
//import com.dkha.entity.FvScDoorRecordEntity;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
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
//@Mapper
//public interface FvScDoorRecordDao extends BaseDao<FvScDoorRecordEntity> {
//    /**
//     * 获取分页数据
//     * @param params
//     * @return
//     */
//    List<FvScDoorRecordEntity> getMyList(Map<String, Object> params);
//
//    /**
//     * 获取分页总数
//     * @param params
//     * @return
//     */
//    Long getMyCount(Map<String, Object> params);
//
//
//    /**
//     * 通过开门记录ID获取记录信息
//     * @param fId
//     * @return
//     */
//    FvScDoorRecordEntity getMyOne(@Param("fId") String fId);
//}