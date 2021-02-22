package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 8英寸智能门禁设备每日采集数量
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@Data
@ApiModel(value = "8英寸智能门禁设备每日采集数量")
public class ScAidooreightDailyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "设备ID")
	private Long aeId;

	@ApiModelProperty(value = "采集时间")
	private Date mdGatherdate;

	@ApiModelProperty(value = "采集数量")
	private Long mdGathercount;

	@ApiModelProperty(value = "更新时间")
	private Date updateDate;

}