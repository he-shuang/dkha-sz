package com.dkha.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScStudentsCountDTO;
import com.dkha.dto.ScStudentsOutEverydayDetailsDTO;
import com.dkha.entity.ScStudentsOutEverydayDetailsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 每日学生未归详情
 *
 * @author Mark 
 * @since v1.0.0 2020-10-15
 */
@Mapper
public interface ScStudentsOutEverydayDetailsDao extends BaseDao<ScStudentsOutEverydayDetailsEntity> {

    IPage<ScStudentsOutEverydayDetailsDTO> findPage(@Param("page") IPage<ScStudentsOutEverydayDetailsEntity> page, @Param("params") Map<String, Object> params);
    IPage<ScStudentsCountDTO> findCountSum(IPage<ScStudentsOutEverydayDetailsEntity> create_date, @Param("params") Map<String, Object> params);
    IPage<ScStudentsOutEverydayDetailsDTO> list(@Param("params") Map<String, Object> params);
    List<ScStudentsCountDTO> findList(@Param("params") Map<String, Object> params);
    List<ScStudentsCountDTO>  findCountRoomExcel(@Param("params") Map<String, Object> params);
    IPage<ScStudentsCountDTO> findCountRoomSum(IPage<ScStudentsOutEverydayDetailsEntity> create_date, @Param("params") Map<String, Object> params);
}