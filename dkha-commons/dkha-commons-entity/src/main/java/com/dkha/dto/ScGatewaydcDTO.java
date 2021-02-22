package com.dkha.dto;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;



/**
 * 网关302设备信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "网关302设备信息")
public class ScGatewaydcDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "网关设备ID")
	private Long gwId;

	@ApiModelProperty(value = "网关设备编号")
	private String gwSn;

	@ApiModelProperty(value = "网关设备名称")
	private String gwName;

	@ApiModelProperty(value = "设备状态：-1 离线 0 正常 1 停用")
	private Integer gwState;

	@ApiModelProperty(value = "设备安装日期")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date gwSetupdate;

	@ApiModelProperty(value = "设备有效期到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date gwExpirydate;

	@ApiModelProperty(value = "设备IP地址")
	private String gwIpgateway;

	@ApiModelProperty(value = "设备安装地址")
	private String[] gwSetupaddr;
	@ApiModelProperty(value = "设备安装地址/")
	private String gwSetupaddrName;
	public String[] getGwSetupaddr() {
		if (null != this.gwSetupaddr) {
			return this.gwSetupaddr;
		}

		return this.gwSetupaddrName.split(",");
	}
	@ApiModelProperty(value = "设备uwb系统对应位置")
	private String gwUwbaddr;

	@ApiModelProperty(value = "uwb区域编号")
	private String gwUwbnum;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "备注")
	private String remark;
	/**
	 * 设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5
	 */
	@ApiModelProperty(value = "设备类型")
	private Integer mbdDevicetype;

	@ApiModelProperty(value = "302网关关联RS485总线")
	private List<ScGatebusdeviceDTO> scGatebusdeviceDTOList;

//	@ApiModelProperty(value = "485总线编号和地址编码集合")
//	private List<ScGatebusdeviceNumDTO> scGatebusdeviceNumDTOList;
}
