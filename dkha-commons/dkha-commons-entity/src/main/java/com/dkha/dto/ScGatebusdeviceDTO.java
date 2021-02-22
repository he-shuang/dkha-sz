package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;


/**
 * 485通讯总线下挂载的设备信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "485通讯总线下挂载的设备信息")
public class ScGatebusdeviceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "网关设备ID")
	private String gwId;

	@ApiModelProperty(value = "通讯设备ID")
	private String mbdId;

	@ApiModelProperty(value = "485通讯总线设备ID")
	private String gbdId;

	@ApiModelProperty(value = "485通讯地址编码")
	private Integer gbdAddr;

	@ApiModelProperty(value = "设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5 ")
	private String gbdDevicetype;

	@ApiModelProperty(value = "设备编号")
	private String gbdDevicesn;
    /**
     * 通讯设备名称
     */
    private String mbdDevicename;

	@ApiModelProperty(value = "485总线编号：1、2、3")
	private String gbdLineNum;

	@ApiModelProperty(value = "分组")
	private String gbdGroup;
    /**
     * uwb围栏ID
     */
    private Long gbdFenceId;


    /**
     * 围栏名称
     */
    private String fenceName;

    /**
     * 灯口编号
     */
    private Integer lightroadnum;
}
