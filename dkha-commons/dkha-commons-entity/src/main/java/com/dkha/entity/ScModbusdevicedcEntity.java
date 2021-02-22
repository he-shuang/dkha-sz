

package com.dkha.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_modbusdevicedc")
public class ScModbusdevicedcEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 通讯设备ID
     */
	@TableId
	private Long mbdId;

    /**
     * 通讯设备编号
     */
	private String mbdDevicesn;

    /**
     * 通讯设备名称
     */
	private String mbdDevicename;

    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
	private Integer mdbStatus;

    /**
     * 设备安装日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date mbdSetupdate;

    /**
     * 设备有效期到期时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date mbdExpirydate;

    /**
     * 设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5
     */
	private Integer mbdDevicetype;

    /**
     * uwb对应位置
     */
	private String mbdUwbaddr;
	@TableField(exist = false)
	private String mbdUwbaddrName;
    /**
     * 楼层ID
     */
	private Long dfFloorid;

	/**
	 * 是否组网:0否；1是
	 */
	private Integer mbdNetwork;

	/**
	 * 备注
	 */
	private String remark;
	@ApiModelProperty(value = "报警状态：1 报警 0 未报警")
	private Integer isAlarm;
	/**
	 * 坐标
	 */
	private String mbdCoordinate;
	/**
	 * 智能灯控设备路数
	 */
	private Integer mbdLightroads;
}
