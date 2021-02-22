package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * 485通讯总线下挂载的设备信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "485通讯总线下挂载的设备信息")
public class ScGatebusdeviceNumDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "485通讯地址编码")
	private Integer gbdAddr;

	@ApiModelProperty(value = "485总线编号：1、2、3")
	private String gbdLineNum;
}
