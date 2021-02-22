package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 宿舍电流每日报警记录
 *
 * @author Mark
 * @since v1.0.0 2020-10-10
 */
@Data
@ApiModel(value = "宿舍电流每日报警记录")
public class ScRoomcurrentEverydayDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "房间编号")
	private String drNum;

	@ApiModelProperty(value = "报警时间")
	private Date alarmDate;

	@ApiModelProperty(value = "报警次数")
	private Integer num;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;

}
