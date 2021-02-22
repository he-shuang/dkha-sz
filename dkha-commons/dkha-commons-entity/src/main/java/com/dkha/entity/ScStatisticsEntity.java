package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since v1.0.0 2020-10-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_statistics")
public class ScStatisticsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
	private Long id;
    /**
     * 周数
     */
	private String week;
	@TableField(exist=false)
	private String weeks;
	@TableField(exist=false)
	private String	months;
	/**
	 * 年月
	 */
	private String month;
    /**
     * 次数
     */
	private String frequency;
    /**
     * 人数
     */
	private String numbersum;
    /**
     * 创建时间
     */
	private Date createDate;
    /**
     * 类型 1宿舍 2教学楼
     */
	private String type;

	/**
	 * 没有进没有出 次数
	 */
	private int type1;
	/**
	 * 只有进 次数
	 */
	private int type2;
	/**
	 * 只有出 次数
	 */
	private int type3;
	/**
	 * 没有进没有出 人数
	 */
	private int typestudent1;
	/**
	 * 只有进 人数
	 */
	private int typestudent2;
	/**
	 * 只有出 人数
	 */
	private int typestudent3;
}
