package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 区域配置/uwb围栏关联
 *
 * @author Mark
 * @since v1.0.0 2020-09-01
 */
@Data
@ApiModel(value = "区域配置/uwb围栏关联")
public class ScRegionConfigDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long rcId;

	@ApiModelProperty(value = "区域名称")
	private String rcName;

	@ApiModelProperty(value = "区域编号")
	private String rcNo;

	@ApiModelProperty(value = "区域地址/位置")
	private String rcAddress;

	@ApiModelProperty(value = "区域楼栋/楼层(两级)")
	private String[] rcFloor;

	@ApiModelProperty(value = "区域楼栋/楼层(两级)名称")
	private String rcFloorName;

	@ApiModelProperty(value = "uwb围栏ID")
	private Long rcFenceId;

	@ApiModelProperty(value = "uwb围栏名称")
	private String rcFenceName;

	@ApiModelProperty(value = "标签类型")
	private Integer rcType;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "修改时间")
	private Date updateDate;

}
