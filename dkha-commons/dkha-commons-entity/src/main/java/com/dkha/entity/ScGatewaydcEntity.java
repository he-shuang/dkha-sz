

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
 * 网关302设备信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_gatewaydc")
public class ScGatewaydcEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 网关设备ID
     */
	@TableId
	private Long gwId;

    /**
     * 网关设备编号
     */
	private String gwSn;

    /**
     * 网关设备名称
     */
	private String gwName;

    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
	private Integer gwState;

    /**
     * 设备安装日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date gwSetupdate;

    /**
     * 设备有效期到期时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date gwExpirydate;

    /**
     * 设备IP地址
     */
	private String gwIpgateway;

    /**
     * 设备安装地址
     */
	private String gwSetupaddr;

	@TableField(exist = false)
	private String gwSetupaddrName;
    /**
     * 设备uwb系统对应位置
     */
	private String gwUwbaddr;

    /**
     * uwb区域编号
     */
	private String gwUwbnum;

    /**
     * 楼层ID
     */
	private Long dfFloorid;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5
	 */
	private Integer mbdDevicetype;
}
