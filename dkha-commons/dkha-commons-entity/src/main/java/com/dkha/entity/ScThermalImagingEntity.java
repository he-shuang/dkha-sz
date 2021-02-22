package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

/**
 * 热成像设备表
 *
 * @author Mark 
 * @since v1.0.0 2020-11-04
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_thermal_imaging")
public class ScThermalImagingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 热成像设备ID
     */
	@TableId
	private Long tfId;
    /**
     * 热成像设备编号
     */
	private String tfDevicesn;
    /**
     * 热成像设备名称
     */
	private String tfDevicename;
    /**
     * 设备类型
     */
	private String tfDevicetype;
    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
	private Integer tfStatus;
    /**
     * 设备安装日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date tfSetupdate;
    /**
     * 设备有效期到期时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date tfExpirydate;
    /**
     * 设备IP地址
     */
	private String tfIpgateway;
    /**
     * 设备安装地址
     */
	/**
	 * 设备安装地址
	 */
	private String tfSetupaddr;
    /**
     * 楼层ID
     */
	private Long dfFloorid;
    /**
     * 备注
     */
	private String remark;
    /**
     * 报警状态： 0 报警 1 未报警
     */
	private Integer isAlarm;
}
