

package com.dkha.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 人证识别设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Data
@ApiModel(value = "人证识别设备")
public class ScPersonidequipDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "设备ID")
	private Long pieId;

	@ApiModelProperty(value = "设备编号")
	private String pieEquipsn;

	@ApiModelProperty(value = "人证设备IP")
	private String pieIpaddr;

	@ApiModelProperty(value = "设备安装地址")
	private String[] pieSetupaddr;

	@ApiModelProperty(value = "通讯设备名称")
	private String pieDevicename;

	@ApiModelProperty(value = "设备状态：-1 离线 0 正常 1 停用")
	private Integer pieStatus;

	@ApiModelProperty(value = "设备安装日期")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date pieSetupdate;

	@ApiModelProperty(value = "设备有效期到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date pieExpirydate;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "是否初始化配置：0 未初始化，1已初始化")
	private Integer pieIsinitial;

	@ApiModelProperty(value = "备注")
	private String remark;
}
