package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 重要设备信息表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_important_device")
public class ScImportantDeviceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 重要设备ID
     */
	@TableId
	private Long imId;
    /**
     * 重要设备编号
     */
	private String imDevicesn;
    /**
     * 重要设备名称
     */
	private String imDevicename;
    /**
     * 设备类型
     */
	private String imDevicetype;
    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
	private Integer imStatus;
    /**
     * 设备安装日期
     */
	private Date imSetupdate;
    /**
     * 设备有效期到期时间
     */
	private Date imExpirydate;
    /**
     * 设备IP地址
     */
	private String imIpgateway;
    /**
     * 设备安装地址
     */
	private String imSetupaddr;
	/**
	 * 设备安装地址/
	 */
	@TableField(exist = false)
	private String imSetupaddrName;
    /**
     * 楼层ID
     */
	private Long dfFloorid;
    /**
     * 备注
     */
	private String remark;
    /**
     * uwb标签号
     */
	private String uwb;
}