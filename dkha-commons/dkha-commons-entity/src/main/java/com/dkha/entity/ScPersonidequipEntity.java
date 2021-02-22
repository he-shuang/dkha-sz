
package com.dkha.entity;


import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 人证识别设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_personidequip")
public class ScPersonidequipEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
	@TableId
	private Long pieId;
    /**
     * 设备编号
     */
	private String pieEquipsn;
    /**
     * 人证设备IP
     */
	private String pieIpaddr;
    /**
     * 设备安装地址
     */
	private String pieSetupaddr;
    /**
     * 通讯设备名称
     */
	private String pieDevicename;
    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
	private Integer pieStatus;
    /**
     * 设备安装日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date pieSetupdate;
    /**
     * 设备有效期到期时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date pieExpirydate;
    /**
     * 楼层ID
     */
	private Long dfFloorid;
    /**
     * 是否初始化配置：0 未初始化，1已初始化
     */
	private Integer pieIsinitial;
    /**
     * 备注
     */
	private String remark;
}
