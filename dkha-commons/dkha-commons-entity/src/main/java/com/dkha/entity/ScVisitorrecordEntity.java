

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 访客记录表
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_visitorrecord")
public class ScVisitorrecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 访客记录Id
     */
	@TableId
	private Long vrId;

    /**
     * 访客姓名
     */
	private String vrName;
	/**
	 * 访客电话
	 */
	private String vrPhone;

    /**
     * 性别：0 男 1女
     */
	private Integer vrSex;

    /**
     * 访客证件身份证号码
     */
	private String vrIdno;

    /**
     * 访客照片地址
     */
	private String vrPhoneimg;

    /**
     * 访客开始时间
     */
	private Date vrVistorbegintime;

    /**
     * 被访人电话
     */
	private String vrInterviewedPhone;
    /**
     * 被访人姓名
     */
	private String vrInterviewed;

	@TableField(exist = false)
	private String vrInterviewedName;

    /**
     * 访问事由
     */
	private String vrReasons;

    /**
     * 访问uwb编号
     */
	private String vrUwbid;

    /**
     * 访客结束时间
     */
	private Date vrVistorendtime;

    /**
     * 是否归还uwb工牌:0 未归还，1 归还
     */
	private Integer vrReturnuwb;

    /**
     * 访客温度
     */
	private BigDecimal vrTemperature;

	/**
	 * 被访人地址信息
	 */
	private String vrAddress;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long creator;

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
	 * 楼层ID
	 */
	private Long dfFloorid;

	/**
	 * 设备编号
	 */
	private String deviceNumber;

	/**
	 * 楼栋名称
	 */
	@TableField(exist = false)
	private String floorname;

	/**
	 * 上级楼栋名称
	 */
	@TableField(exist = false)
	private String parentfloorname;
}
