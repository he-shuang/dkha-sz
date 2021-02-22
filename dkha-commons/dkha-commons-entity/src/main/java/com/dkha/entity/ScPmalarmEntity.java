

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * PM2.5设备报警信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_pmalarm")
public class ScPmalarmEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * PM2.5报警ID
     */
	@TableId
	private Long pmaId;

    /**
     * PM设备序列号
     */
	private String pmaDevicesn;

    /**
     * PM设备ID
     */
	private Long pmaDeviceid;

    /**
     * PM采集值
     */
	private BigDecimal pmaValue;

    /**
     * PM报警时间
     */
	private Date pmaAlarmtime;

    /**
     * 报警对应位置
     */
	private String pmaAddress;

	/**
	 * 环境温度
	 */
	private BigDecimal pmaTemperature;

	/**
	 * 环境湿度
	 */
	private BigDecimal pmaHumidity;
	/**
	 * 是否已处理
	 */
	private Integer pmaIshandle;
	/**
	 * 处理时间
	 */
	private Date pmaHandletime;

	@TableField(exist = false)
	private String devicename;
}
