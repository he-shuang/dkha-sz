package com.dkha.dao;

import com.dkha.commons.mybatis.dao.BaseDao;
import com.dkha.entity.ScDeptDoorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 8寸门禁与部门对于关系
 */
@Mapper
public interface ScDeptDoorDao extends BaseDao<ScDeptDoorEntity> {

    @Update("REPLACE INTO sc_dept_door_scope(door_id,dept_id,creator) VALUE(#{doorId},#{deptId},#{creator})")
    int insertOrUpdate(ScDeptDoorEntity deptDoorEntity);


    @Select("SELECT sc.id,sc.`dept_id`,sc.`door_id`,sc.`creator`,sd.`name` deptName FROM sc_dept_door_scope sc,sys_dept sd WHERE sc.`dept_id`=sd.`id` AND door_id = #{doorId}")
    List<ScDeptDoorEntity> selectDeptDoorByDoorId(long doorId);
}
