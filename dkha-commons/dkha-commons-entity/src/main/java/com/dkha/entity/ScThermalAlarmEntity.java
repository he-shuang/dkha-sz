package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 热成像报警表
 *
 * @author Mark
 * @since v1.0.0 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_thermal_alarm")
public class ScThermalAlarmEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 热成像报警ID
     */
	@TableId
	private Long tfaId;
    /**
     * 热成像编号
     */
	private String tfDevicesn;
    /**
     * 热成像设备ID
     */
	private Long tfId;
    /**
     * 报警温度值
     */
	private Float tfaEcurrent;
    /**
     * 报警时间
     */
	private Date tfaAlarmtime;
    /**
     * 备注
     */
	private String remark;
    /**
     * 是否已处理 0 未处理 1 已处理
     */
	private Integer tfaIshandle;

	/**
	 * 设备名称
	 */
	@TableField(exist = false)
	private String tfDevicename;

	/**
	 * 设备ip
	 */
	@TableField(exist = false)
	private String tfIpgateway;

	/**
	 * 设备类型
	 */
	@TableField(exist = false)
	private String tfDevicetype;

	/**
	 * 设备安装位置
	 */
	@TableField(exist = false)
	private String tfSetupaddr;
	/**
	 * 是否报警
	 */
	@TableField(exist = false)
	private boolean needAlarm;
}
