package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 消费系统会员信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Data
@ApiModel(value = "消费系统会员信息")
public class ScConsumptionsystemVipDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "会员姓名")
	private String name;

	@ApiModelProperty(value = "编号（学号、职工编号）")
	private String number;

	@ApiModelProperty(value = "身份证")
	private String idno;

	@ApiModelProperty(value = "会员卡号")
	private String cardId;

	@ApiModelProperty(value = "账户总余额")
	private BigDecimal balance;

	@ApiModelProperty(value = "赠送余额")
	private BigDecimal giveBalance;

	@ApiModelProperty(value = "本金余额")
	private BigDecimal rechargeBalance;

	@ApiModelProperty(value = "手机号")
	private String phone;

	@ApiModelProperty(value = "身份类别（学生、教职工、其他）")
	private String type;

	@ApiModelProperty(value = "更新时间")
	private String updateDate;

	@ApiModelProperty(value = "旧卡号")
	private String oldCardId;

	@ApiModelProperty(value = "注册时间")
	private String registerDate;

	@ApiModelProperty(value = "累计付款金额")
	private BigDecimal totalPayAmt;

	@ApiModelProperty(value = "累计消费金额")
	private BigDecimal costTotal;

	@ApiModelProperty(value = "累计消费次数")
	private String costTotalNum;

	@ApiModelProperty(value = "1.正常 2.挂失 3. 注销")
	private Integer state;

}