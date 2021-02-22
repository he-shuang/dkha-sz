package com.dkha.dao;


import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.dto.ScStudentsDTO;
import com.dkha.entity.ScStudentsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 学生档案信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScStudentsDao extends BaseDao<ScStudentsEntity> {

    List<ScStudentsEntity> getByScNos(List<String> scNo);

    ScStudentsEntity getByScNo(String scNo);

    List<ScStudentsDTO> getStudentsInfo(@Param("pIds") List<Long> pIds, @Param("flag") int flag);

    List<Map<Object,Object>> selectDoorByIds(@Param("ids")List<String> ids);

    Integer getUserId();

    List<Map<Object,Object>> selectWorkerByIds(@Param("ids")List<String> ids);

    List<String> getAllId();
}
