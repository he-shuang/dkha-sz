package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 消费系统的消费记录
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Data
@ApiModel(value = "消费系统的消费记录")
public class ScConsumptionsystemConsumeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "会员卡号")
	private String cardId;

	@ApiModelProperty(value = "会员名称")
	private String name;

	@ApiModelProperty(value = "消费总金额")
	private BigDecimal costTotal;

	@ApiModelProperty(value = "消费实际金额")
	private BigDecimal actualAmt;

	@ApiModelProperty(value = "消费优惠金额")
	private BigDecimal discountAmt;

	@ApiModelProperty(value = "消费时间")
	private String createDate;

	@ApiModelProperty(value = "消费商家名称")
	private String business;

	@ApiModelProperty(value = "消费单号")
	private String billid;

}