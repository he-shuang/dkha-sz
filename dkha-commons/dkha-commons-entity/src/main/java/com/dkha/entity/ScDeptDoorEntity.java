package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 8寸门禁与部门对于关系
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_dept_door_scope")
public class ScDeptDoorEntity implements Serializable {

    @TableId
    private Long id;
    private Long doorId;
    private Long deptId;
    private Long creator;
    @TableField(exist = false)
    private String deptName;
}
