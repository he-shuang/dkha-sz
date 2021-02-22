package com.dkha.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;


/**
 * 485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备")
public class ScModbusdevicedcDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "通讯设备ID")
	private Long mbdId;

	@ApiModelProperty(value = "通讯设备编号")
	private String mbdDevicesn;

	@ApiModelProperty(value = "通讯设备名称")
	private String mbdDevicename;

	@ApiModelProperty(value = "设备状态：-1 离线 0 正常 1 停用")
	private Integer mdbStatus;

	@ApiModelProperty(value = "设备安装日期")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date mbdSetupdate;

	@ApiModelProperty(value = "设备有效期到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date mbdExpirydate;

	@ApiModelProperty(value = "设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5 ")
	private Integer mbdDevicetype;

	@ApiModelProperty(value = "uwb对应位置")
	private String[] mbdUwbaddr;

	@ApiModelProperty(value = "设备安装地址/")
	private String mbdUwbaddrName;
	public String[] getMbdUwbaddr() {
		if (null != this.mbdUwbaddr) {
			return this.mbdUwbaddr;
		}
		if(StringUtils.isBlank(this.mbdUwbaddrName)) {
			return null;
		}
		return this.mbdUwbaddrName.split(",");
	}
	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "是否组网:0否；1是")
	private Integer mbdNetwork;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "报警状态： 0 报警 1 未报警")
	private Integer isAlarm;

	@ApiModelProperty(value = "坐标")
	private String mbdCoordinate;

	@ApiModelProperty(value = "智能灯控设备路数")
	private Integer mbdLightroads;
}
