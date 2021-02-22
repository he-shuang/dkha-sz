package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author dkha 
 * @since v1.0.0 2020-10-22
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_students_inandout")
public class ScStudentsInandoutEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 学生出进记录次数id
     */
	@TableId
	private Long id;
    /**
     * 学生档案ID
     */
	private Long scStdid;
    /**
     * 进入次数
     */
	private String inSum;
    /**
     * 出门次数
     */
	private String outSum;
    /**
     * 创建时间
     */
	private Date createDate;
	/**
	 * 类型： 1宿舍 2教学楼
	 */
	private int type;
	/**
	 * 没有进没有出
	 */
	private int type1;
	/**
	 * 只有进
	 */
	private int type2;
	/**
	 * 只有出
	 */
	private int type3;
}
