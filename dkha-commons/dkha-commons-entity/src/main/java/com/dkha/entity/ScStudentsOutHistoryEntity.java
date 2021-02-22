package com.dkha.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生未归寝每日统计
 *
 * @author Mark
 * @since v1.0.0 2020-10-09
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_students_out_history")
public class ScStudentsOutHistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
	private Long id;
    /**
     * 统计时间
     */
	private Date date;
    /**
     * 人数
     */
	private Integer num;
}
