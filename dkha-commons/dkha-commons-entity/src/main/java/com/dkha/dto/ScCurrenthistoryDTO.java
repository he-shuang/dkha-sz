package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面")
public class ScCurrenthistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "电流互感器采集ID")
	private Long chId;

	@ApiModelProperty(value = "电流互感器采集设备编号")
	private String chDevicesn;

	@ApiModelProperty(value = "采集设备ID")
	private Long chDeviceid;

	@ApiModelProperty(value = "采集电流值")
	private Float chEcurrent;

	@ApiModelProperty(value = "采集时间")
	private Date chColltime;

	@ApiModelProperty(value = "房间编号")
	private String chNum;

	@ApiModelProperty(value = "房间ID")
	private Long chRoomid;

	@ApiModelProperty(value = "楼层ID")
	private Long floorid;

}
