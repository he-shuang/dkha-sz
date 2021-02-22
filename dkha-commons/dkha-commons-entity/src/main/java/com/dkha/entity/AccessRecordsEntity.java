package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 出入记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-30
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_access_records")
public class AccessRecordsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId
	private Long id;
    /**
     * 照片地址
     */
	private String imgUrl;
    /**
     * 姓名
     */
	private String name;
    /**
     * 学号
     */
	private String studentNum;
    /**
     * 宿舍号
     */
	private String dormitoryNum;
    /**
     * 状态(1:未归 0 : 已归)
     */
	private Integer status;
    /**
     * 外出时间
     */
	private Date outTime;
    /**
     * 归来时间
     */
	private Date backTime;

	/**
	 * 创建时间
	 */
	private Date createDate;
}
