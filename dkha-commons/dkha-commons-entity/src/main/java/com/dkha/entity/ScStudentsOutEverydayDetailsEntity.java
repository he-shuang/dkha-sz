package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 每日学生未归详情
 *
 * @author Mark 
 * @since v1.0.0 2020-10-15
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_students_out_everyday_details")
public class ScStudentsOutEverydayDetailsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
	private Long id;
    /**
     * 学生ID
     */
	private Long scStdid;
    /**
     * 创建时间
     */
	private Date createDate;
}
