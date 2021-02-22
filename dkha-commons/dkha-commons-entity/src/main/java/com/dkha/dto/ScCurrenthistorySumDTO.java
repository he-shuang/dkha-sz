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
 * @since v1.0.0 2020-11-06
 */
@Data
@ApiModel(value = "")
public class ScCurrenthistorySumDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "房间编号")
	private String chNum;

	@ApiModelProperty(value = "房间id")
	private Long chRoomid;

	@ApiModelProperty(value = "电流值")
	private Float chEcurrent;

	@ApiModelProperty(value = "楼层id")
	private Long chFloorid;

	@ApiModelProperty(value = "采集时间")
	private Date chColltime;

}