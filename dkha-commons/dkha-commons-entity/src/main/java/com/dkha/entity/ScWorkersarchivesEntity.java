

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 教职工档案
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_workersarchives")
public class ScWorkersarchivesEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 职工档案ID
     */
	@TableId
	private Long scWaid;

    /**
     * 职工姓名
     */
	private String scWaname;

    /**
     * 性别：0男 1女
     */
	private Integer scWasex;

    /**
     * 职工编号
     */
	private String scEmpno;

    /**
     * 职工类型：0 教师 1 保洁 2 保安
     */
	private Integer scEmptype;

    /**
     * 状态： -1 离职  0 正常
     */
	private Integer scStatus;

    /**
     * 职工照片地址
     */
	private String scPhotoimg;

    /**
     * 职工RFCardid编号
     */
	private String scRfcardid;

    /**
     * 职工uwb编号
     */
	private String scUwbid;

    /**
     * 职工手机号
     */
	private String scPhonenum;

    /**
     * 职工入职时间
     */
	private String scHiredate;

    /**
     * 身份证号
     */
	private String scIdno;

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
}
