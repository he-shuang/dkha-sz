package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 刷脸记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-15
 */
@Data
@ApiModel(value = "刷脸或卡记录表")
public class ScFaceverificationDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "记录Id ")
	private Long fvId;

	@ApiModelProperty(value = "设备Id")
	private Long equipmentId;

	@ApiModelProperty(value = "唯一标识码")
	private String equipmentVerificationId;

	@ApiModelProperty(value = "人员编号(当verificationType为-1时，该参数不上传)")
	private String personCode;

	@ApiModelProperty(value = "签到类型(-1：识别失败； 1：签到； 2：签退； 3：签到、签退； 4：识别成功，但没有门禁权限；)")
	private Integer verificationType;

	@ApiModelProperty(value = "打卡时间戳毫秒")
	private Long checkTime;

	@ApiModelProperty(value = "图片名称")
	private String imageName;

	@ApiModelProperty(value = "识别人员名称")
	private String recognitionName;

	@ApiModelProperty(value = "IC卡号")
	private String icCardNo;

	@ApiModelProperty(value = "是否包含图片(1：有图片；2：无图片)")
	private Integer existImage;

	@ApiModelProperty(value = "记录类型(1：刷脸记录；2：刷IC卡记录)")
	private Integer recordType;

	@ApiModelProperty(value = "预警温度值")
	private BigDecimal warningTemperature;

	@ApiModelProperty(value = "检测温度值")
	private BigDecimal temperature;

	@ApiModelProperty(value = "打卡时间")
	private Date createDate;

	@ApiModelProperty(value = "图片地址")
	private String imageUrl;

	@ApiModelProperty(value = "图片地址Byte")
	private byte[] imageUrlByte;

	@ApiModelProperty(value = "设备名称")
	private String aeDevicename;

}
