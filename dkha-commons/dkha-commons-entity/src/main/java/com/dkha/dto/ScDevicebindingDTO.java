package com.dkha.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;


/**
 * 星网云联设备位置绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-27
 */
@Data
@ApiModel(value = "星网云联设备位置绑定表")
public class ScDevicebindingDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "设备id(所有设备)")
	private String deviceId;

	@ApiModelProperty(value = "星网云联坐标")
	private String coordinate;

	@ApiModelProperty(value = "地图id")
	private Integer mapId;

	@ApiModelProperty(value = "地图楼层")
	private String floor;

	@ApiModelProperty(value = "备注信息")
	private String remark;

	private Date createDate;
	@TableField(exist = false)
	private Long deid;
	@TableField(exist = false)
	private Integer status;
	@TableField(exist = false)
	private String devicename;
	@TableField(exist = false)
	private String type;
	@TableField(exist = false)
	private String device;

}