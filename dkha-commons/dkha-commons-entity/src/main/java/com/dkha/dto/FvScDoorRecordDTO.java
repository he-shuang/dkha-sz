package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 5寸门禁开门记录
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Data
@ApiModel(value = "5寸门禁开门记录")
public class FvScDoorRecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Integer fId;

	@ApiModelProperty(value = "添加时间")
	private Integer fAddTime;

	@ApiModelProperty(value = "权限ID")
	private String fKeyId;

	@ApiModelProperty(value = "设备ID")
	private Integer fDeviceId;

	@ApiModelProperty(value = "开门类型,1- 卡号 2- 注册用户id   3-蓝牙发的卡号（手机号） 4-密码开门   5-指纹ID  6-人脸ID")
	private Integer fOpenType;

	@ApiModelProperty(value = "通过时间")
	private Integer fOpenTime;

	@ApiModelProperty(value = "合法标志, 0-非法 1-合法")
	private Integer fState;

	@ApiModelProperty(value = "人员ID")
	private Integer fPersonId;

	@ApiModelProperty(value = "邀请人")
	private String fInviterName;

	@ApiModelProperty(value = "代理商")
	private Integer fAgentId;

	@ApiModelProperty(value = "下发人脸地址")
	private String fFaceUrl;

	@ApiModelProperty(value = "添加时间")
	private Date fAddDate;

	@ApiModelProperty(value = "姓名")
	private String fName;
	/**
	 * 员设备名称
	 */
	private String deviceName;

	/**
	 * 设备编号
	 */
	private String fSerialNumber;

}