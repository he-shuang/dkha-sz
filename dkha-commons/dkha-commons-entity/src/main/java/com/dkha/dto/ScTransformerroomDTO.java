package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 互感器宿舍关联关系
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "互感器宿舍关联关系")
public class ScTransformerroomDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "互感器关联房间ID")
	private Long tfrId;

	@ApiModelProperty(value = "房间ID")
	private Long drId;

	@ApiModelProperty(value = "互感器设备ID")
	private Long tfId;

	@ApiModelProperty(value = "设备编号")
	private String tfDevicesn;

	@ApiModelProperty(value = "宿舍房间编号")
	private String tfrDrroomno;

	@ApiModelProperty(value = "互感器端口地址：0-3")
	private Integer tfrPortaddr;

	@ApiModelProperty(value = "互感器关联时间")
	private Date tfrRelationdate;

	private String tfIpgateway;


}
