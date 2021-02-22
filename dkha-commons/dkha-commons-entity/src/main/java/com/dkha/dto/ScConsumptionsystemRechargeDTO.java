package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 消费系统充值信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Data
@ApiModel(value = "消费系统充值信息")
public class ScConsumptionsystemRechargeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "会员卡号")
	private String cardId;

	@ApiModelProperty(value = "会员名称")
	private String name;

	@ApiModelProperty(value = "充值总金额")
	private BigDecimal rechargeTotalAmt;

	@ApiModelProperty(value = "充值金额")
	private BigDecimal rechargeAmt;

	@ApiModelProperty(value = "充值赠送金额")
	private BigDecimal giveAmt;

	@ApiModelProperty(value = "充值时间")
	private String createDate;

	@ApiModelProperty(value = "充值方式(1.现金2.POS 3.微信 4.支付宝 5. 1其他)")
	private Integer type;

	@ApiModelProperty(value = "操作人")
	private String operName;

}