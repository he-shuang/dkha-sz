package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费系统充值信息
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_consumptionsystem_recharge")
public class ScConsumptionsystemRechargeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
	@TableId
	private Long id;
    /**
     * 会员卡号
     */
	private String cardId;
	/**
	 * 会员名称
	 */
	@TableField(exist = false)
	private String name;
    /**
     * 充值总金额
     */
	private BigDecimal rechargeTotalAmt;
    /**
     * 充值金额
     */
	private BigDecimal rechargeAmt;
    /**
     * 充值赠送金额
     */
	private BigDecimal giveAmt;
    /**
     * 充值时间
     */
	private String createDate;
    /**
     * 充值方式(1.现金2.POS 3.微信 4.支付宝 5. 1其他)
     */
	private Integer type;
    /**
     * 操作人
     */
	private String operName;
}
