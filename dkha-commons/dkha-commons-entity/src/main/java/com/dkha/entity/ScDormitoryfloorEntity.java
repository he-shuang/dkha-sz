

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 楼栋，楼层信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_dormitoryfloor")
public class ScDormitoryfloorEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 楼层ID
     */
	@TableId
	private Long dfFloorid;

    /**
     * 楼层/楼栋名称
     */
	private String dfFloorname;

    /**
     * 上级楼层信息ID
     */
	private Long dfParentid;

    /**
     * 楼层类型：0 楼栋，1 楼层
     */
	private Integer dfType;

    /**
     * 排序序号：数字从小到大显示
     */
	private Integer dfOrder;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long  creator;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;
	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;

    /**
     * 使用用途：0 学生宿舍  1 教师宿舍 2 教学楼
     */
	private Integer dfPurpose;

	/**
	 * 是否叶子节点  0：否   1：是
	 */
	private Integer leaf;

	/**
	 * 上级名称
	 */
	@TableField(exist = false)
	private String parentName;
}
