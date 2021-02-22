

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 485通讯总线下挂载的设备信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_gatebusdevice")
public class ScGatebusdeviceEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 网关设备ID
     */
	@TableId
	private String gwId;

    /**
     * 通讯设备ID
     */
	private String mbdId;

    /**
     * 485通讯总线设备ID
     */
	private String gbdId;

    /**
     * 485通讯地址编码
     */
	private Integer gbdAddr;

    /**
     * 设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5
     */
	private String gbdDevicetype;

    /**
     * 设备编号
     */
	private String gbdDevicesn;

	/**
	 * 485总线编号：1、2、3
	 */
	private String gbdLineNum;

    /**
     * uwb围栏ID
     */
    private Long gbdFenceId;

    /**
     * 围栏名称
     */
    private String fenceName;

    /**
	 * 分组
	 */
	private String gbdGroup;

    /**
	 * 灯口编号
	 */
	private Integer lightroadnum;


}
