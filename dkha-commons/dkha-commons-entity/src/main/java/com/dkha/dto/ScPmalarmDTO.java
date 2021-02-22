package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;


/**
 * PM2.5设备报警信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "PM2.5设备报警信息")
public class ScPmalarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "PM2.5报警ID")
	private Long pmaId;

	@ApiModelProperty(value = "PM设备序列号")
	private String pmaDevicesn;

	@ApiModelProperty(value = "PM设备ID")
	private Long pmaDeviceid;

	@ApiModelProperty(value = "PM采集值")
	private BigDecimal pmaValue;

	@ApiModelProperty(value = "PM报警时间")
	private Date pmaAlarmtime;

	@ApiModelProperty(value = "报警对应位置")
	private String pmaAddress;

	@ApiModelProperty(value = "环境温度")
	private BigDecimal pmaTemperature;

	@ApiModelProperty(value = "环境湿度")
	private BigDecimal pmaHumidity;

	@ApiModelProperty(value = "是否已处理")
	private Integer pmaIshandle;

	@ApiModelProperty(value = "处理时间")
	private Date pmaHandletime;
	@ApiModelProperty(value = "设备名称")
	private String devicename;
}
