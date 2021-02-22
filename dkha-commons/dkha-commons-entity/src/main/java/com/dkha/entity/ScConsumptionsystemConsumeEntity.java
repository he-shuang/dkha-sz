package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费系统的消费记录
 *
 * @author Mark 
 * @since v1.0.0 2020-10-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_consumptionsystem_consume")
public class ScConsumptionsystemConsumeEntity implements Serializable {
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
     * 消费总金额
     */
	private BigDecimal costTotal;
    /**
     * 消费实际金额
     */
	private BigDecimal actualAmt;
    /**
     * 消费优惠金额
     */
	private BigDecimal discountAmt;
    /**
     * 消费时间
     */
	private String createDate;
    /**
     * 消费商家名称
     */
	private String business;
    /**
     * 消费单号
     */
	private String billid;
}
