package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * All rights 成都电科慧安
 *
 * @ClassName: ScUwbLabelToInfoWSDTO
 * @program: dkha-cloud
 * @description:
 * @author: linhuacheng
 * @create: 2020/8/23 11:09
 */
@Data
@ApiModel(value = "UWB标签绑定的信息")
public class ScUwbLabelToInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "对象姓名")
	private String personName;

	@ApiModelProperty(value = "对象图片")
	private String personPic;

	@ApiModelProperty(value = "是否开启定位 1开启 0 关闭")
	private Integer personState;

	@ApiModelProperty(value = "是否显示头像 1开启 0 关闭")
	private Integer iconState;

	@ApiModelProperty(value = "UWB人员角色ID")
	private Long departmentId;

	@ApiModelProperty(value = "标签类型")
	private String tagType;

	@ApiModelProperty(value = "标签ID")
	private Long uwbId;

	@ApiModelProperty(value = "类型")
	private Integer emptype;

	@ApiModelProperty(value = "更多信息json")
	private String moreinfo;

}
