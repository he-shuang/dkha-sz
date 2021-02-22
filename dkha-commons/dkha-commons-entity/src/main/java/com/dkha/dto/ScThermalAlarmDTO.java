package com.dkha.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 热成像报警表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
@Data
@ApiModel(value = "热成像报警表")
public class ScThermalAlarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "热成像报警ID")
	private Long tfaId;

	@ApiModelProperty(value = "热成像编号")
	private String tfDevicesn;

	@ApiModelProperty(value = "热成像设备ID")
	private Long tfId;

	@ApiModelProperty(value = "报警温度值")
	private Float tfaEcurrent;

	@ApiModelProperty(value = "报警时间")
	private Date tfaAlarmtime;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "是否已处理 0 未处理 1 已处理")
	private Integer tfaIshandle;

	@ApiModelProperty(value = "设备名称")
	private String tfDevicename;

	@ApiModelProperty(value = "设备ip")
	private String tfIpgateway;

	@ApiModelProperty(value = "设备类型")
	private String tfDevicetype;

	@ApiModelProperty(value = "设备安装地址")
	private String tfSetupaddr;

}