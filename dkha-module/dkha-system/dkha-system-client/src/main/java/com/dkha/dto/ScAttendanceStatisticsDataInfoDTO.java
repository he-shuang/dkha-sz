package com.dkha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 考勤统计
 *
 * @author Mark 
 * @since v1.0.0 2020-12-14
 */
@Data
@ApiModel(value = "考勤统计")
public class ScAttendanceStatisticsDataInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "统计日期")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date stDate;

	@ApiModelProperty(value = "次数")
	private Integer stNum;


}