

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
 * 互感器设备信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_transformerdc")
public class ScTransformerdcEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 互感器设备ID
     */
	@TableId
	private Long tfId;

    /**
     * 互感器设备编号
     */
	private String tfDevicesn;

    /**
     * 互感器设备名称
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
	private String tfSetupaddr;

    /**
     * 楼层ID
     */
	private Long dfFloorid;

	/**
	 * 备注
	 */
	private String remark;
	@ApiModelProperty(value = "报警状态： 0 报警 1 未报警")
	private Integer isAlarm;

}
