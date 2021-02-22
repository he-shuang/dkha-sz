package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScDormitoryfloorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 楼栋，楼层信息
 *
 * @since v1.0.0 2020-08-23
 */
@Mapper
public interface ScDormitoryfloorDao extends BaseDao<ScDormitoryfloorEntity> {

    List<ScDormitoryfloorEntity> getList(Map<String, Object> params);

    ScDormitoryfloorEntity getByPId(Long dfParentid);

    int getCountByPid(Long dfFloorid);

    List<Map<String, Object>> getTreeList(Integer dfPurpose);

    String findName(String s);

    List<Map<String, Object>> roleTree(@Param("dfPurposes") List<Integer> dfPurposes);
}
