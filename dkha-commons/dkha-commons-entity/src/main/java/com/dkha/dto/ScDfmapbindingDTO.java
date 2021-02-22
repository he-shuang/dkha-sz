package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 楼层与星网地图绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-29
 */
@Data
@ApiModel(value = "楼层与星网地图绑定表")
public class ScDfmapbindingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "地图楼栋ID")
	private Long mapId;

	@ApiModelProperty(value = "地图楼层")
	private String floor;


}