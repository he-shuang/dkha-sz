package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


/**
 * 星网云联设备位置绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_devicebinding")
public class ScDevicebindingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 设备id(所有设备)
     */
	@TableId
	private Long id;

	private String deviceId;
    /**
     * 星网云联坐标
     */
	private String coordinate;
    /**
     * 地图id
     */
	private Integer mapId;
    /**
     * 地图楼层
     */
	private String floor;
    /**
     * 备注信息
     */
	private String remark;

	private Date createDate;
	@TableField(exist = false)
	private Integer status;

	@TableField(exist = false)
	private Long deid;
	@TableField(exist = false)
	private String devicename;
	@TableField(exist = false)
	private String type;
	@TableField(exist = false)
	private String device;
}