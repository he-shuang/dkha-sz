package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 电流互感器房间电流信息报警
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "电流互感器房间电流信息报警")
public class ScTransformalarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "电流互感器报警ID")
	private Long tfaId;

	@ApiModelProperty(value = "互感器设备编号")
	private String tfDevicesn;

	@ApiModelProperty(value = "互感器设备ID")
	private Long tfId;

	@ApiModelProperty(value = "报警电流值")
	private Float tfaEcurrent;

	@ApiModelProperty(value = "报警时间")
	private Date tfaAlarmtime;

	@ApiModelProperty(value = "房间编号")
	private String drNum;

	@ApiModelProperty(value = "房间ID")
	private Long drRoomid;

	@ApiModelProperty(value = "房间楼栋名称+房间编号 组合信息")
	private String roomName;
	@ApiModelProperty(value = "报警是否已处理 0 为处理 1 已处理")
	private Integer tfaIshandle;

	@ApiModelProperty(value = "报警处理时间")
	private Date tfaHandletime;
}
