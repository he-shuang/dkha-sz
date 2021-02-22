package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 考勤统计
 *
 * @author Mark 
 * @since v1.0.0 2020-12-14
 */
@Data
@ApiModel(value = "考勤统计")
public class ScAttendanceStatisticsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "房间名称(8寸门禁设备名称)")
	private String aeDevicename;

	@ApiModelProperty(value = "8寸门禁设备ID")
	private Long aeId;

	@ApiModelProperty(value = "职工ID")
	private Long scWaid;

	@ApiModelProperty(value = "职工姓名")
	private String scWaname;

	@ApiModelProperty(value = "统计日期")
	private Date stDate;

	@ApiModelProperty(value = "次数")
	private Integer stNum;

}