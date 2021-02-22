package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 刷脸记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-15
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_faceverification")
public class ScFaceverificationEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 记录Id 
     */
	@TableId
	private Long fvId;
    /**
     * 设备Id
     */
	private Long equipmentId;
    /**
     * 唯一标识码
     */
	private String equipmentVerificationId;
    /**
     * 人员编号(当verificationType为-1时，该参数不上传)
     */
	private String personCode;
    /**
     * 签到类型(-1：识别失败； 1：签到； 2：签退； 3：签到、签退； 4：识别成功，但没有门禁权限；)
     */
	private Integer verificationType;
    /**
     * 打卡时间戳毫秒
     */
	private Long checkTime;
    /**
     * 图片名称
     */
	private String imageName;
    /**
     * 识别人员名称
     */
	private String recognitionName;
    /**
     * IC卡号
     */
	private String icCardNo;
    /**
     * 是否包含图片(1：有图片；2：无图片)
     */
	private Integer existImage;
    /**
     * 记录类型(1：刷脸记录；2：刷IC卡记录)
     */
	private Integer recordType;
    /**
     * 预警温度值
     */
	private BigDecimal warningTemperature;
    /**
     * 检测温度值
     */
	private BigDecimal temperature;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 图片地址
	 */
	private String imageUrl;
	/**
	 * 设备名称
	 */
	@TableField(exist = false)
	private String aeDevicename;

	/**
	 * 进出数量
	 */
	@TableField(exist = false)
	private String sum;

}