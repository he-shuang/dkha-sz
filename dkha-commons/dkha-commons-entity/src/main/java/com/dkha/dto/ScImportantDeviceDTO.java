package com.dkha.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 重要设备信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Data
@ApiModel(value = "重要设备信息表")
public class ScImportantDeviceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "重要设备ID")
	private Long imId;

	@ApiModelProperty(value = "重要设备编号")
	private String imDevicesn;

	@ApiModelProperty(value = "重要设备名称")
	private String imDevicename;

	@ApiModelProperty(value = "设备类型")
	private String imDevicetype;

	@ApiModelProperty(value = "设备状态：-1 离线 0 正常 1 停用")
	private Integer imStatus;

	@ApiModelProperty(value = "设备安装日期")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date imSetupdate;

	@ApiModelProperty(value = "设备有效期到期时间")
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date imExpirydate;

	@ApiModelProperty(value = "设备IP地址")
	private String imIpgateway;

	@ApiModelProperty(value = "设备安装地址")
	private String[] imSetupaddr;
	@TableField(exist = false)
	private String imSetupaddrName;
	public String[] getImSetupaddr() {
		if (null != this.imSetupaddr) {
			return this.imSetupaddr;
		}

		return this.imSetupaddrName.split(",");
	}
	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "uwb标签号")
	private String uwb;


}