package com.dkha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 热成像设备表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
@Data
@ApiModel(value = "热成像设备表")
public class ScThermalImagingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "热成像设备ID")
	private Long tfId;

	@ApiModelProperty(value = "热成像设备编号")
	private String tfDevicesn;

	@ApiModelProperty(value = "热成像设备名称")
	private String tfDevicename;

	@ApiModelProperty(value = "设备类型")
	private String tfDevicetype;

	@ApiModelProperty(value = "设备状态：-1 离线 0 正常 1 停用")
	private Integer tfStatus;

	@ApiModelProperty(value = "设备安装日期")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date tfSetupdate;

	@ApiModelProperty(value = "设备有效期到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date tfExpirydate;

	@ApiModelProperty(value = "设备IP地址")
	private String tfIpgateway;

	@ApiModelProperty(value = "设备安装地址")
	private String[] tfSetupaddr;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "报警状态： 0 报警 1 未报警")
	private Integer isAlarm;

}