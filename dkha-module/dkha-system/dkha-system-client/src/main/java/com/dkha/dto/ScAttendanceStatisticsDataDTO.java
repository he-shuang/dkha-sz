package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 考勤统计
 *
 * @author Mark 
 * @since v1.0.0 2020-12-14
 */
@Data
@ApiModel(value = "考勤统计")
public class ScAttendanceStatisticsDataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "房间名称(8寸门禁设备名称)")
	private String aeDevicename;

	@ApiModelProperty(value = "职工姓名")
	private String scWaname;

	@ApiModelProperty("详情")
	private List<ScAttendanceStatisticsDataInfoDTO> scAttendanceStatisticsDataInfoDTOList;

}