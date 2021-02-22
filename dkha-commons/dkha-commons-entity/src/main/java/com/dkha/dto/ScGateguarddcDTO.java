package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 *
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "")
public class ScGateguarddcDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门禁设备ID")
	private Long ggdId;

	@ApiModelProperty(value = "门禁设备编号")
	private String ggdSn;

	@ApiModelProperty(value = "设备名称")
	private String ggdName;

	@ApiModelProperty(value = "设备型号")
	private String ggdType;

	@ApiModelProperty(value = "设备状态：-1 离线 0 正常 1停用")
	private Integer ggdStatus;

	@ApiModelProperty(value = "设备安装日期")
	private Date ggdSetupdate;

	@ApiModelProperty(value = "设备有效期到期日期")
	private Date ggdExpirydate;

	@ApiModelProperty(value = "设备IP地址")
	private String ggdIpgateway;

	@ApiModelProperty(value = "设备安装地址")
	private String ggdSetupaddr;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "房间ID")
	private Long drId;

}
