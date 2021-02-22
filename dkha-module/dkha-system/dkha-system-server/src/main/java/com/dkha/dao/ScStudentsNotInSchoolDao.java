package com.dkha.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScStudentsNotInSchoolDTO;
import com.dkha.entity.ScStudentsEntity;
import com.dkha.entity.ScStudentsNotInSchoolEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 学生未归寝每日统计 Mapper 接口
 * </p>
 *
 * @author Spring
 * @since 2020-12-07
 */
@Mapper
public interface ScStudentsNotInSchoolDao extends BaseDao<ScStudentsNotInSchoolEntity> {

    /**
     * 分页查询
     * @param page
     * @param params
     * @return
     */
    IPage<ScStudentsNotInSchoolEntity> findPage(@Param("page") IPage<ScStudentsNotInSchoolEntity> page, @Param("params") Map<String, Object> params);

    List<Map<String,Object>> getInSchoolRecord(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<ScStudentsEntity> getStudents(List<String> sns);

    Map<String,Object> getGoOutMsg(@Param("scNo") String scNo, @Param("currentDate") String currentDate);
}
