package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 出入记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-30
 */
@Data
@ApiModel(value = "出入记录")
public class AccessRecordsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键")
	private Long id;

	@ApiModelProperty(value = "照片地址")
	private String imgUrl;

	@ApiModelProperty(value = "姓名")
	private String name;

	@ApiModelProperty(value = "学号")
	private String studentNum;

	@ApiModelProperty(value = "宿舍号")
	private String dormitoryNum;

	@ApiModelProperty(value = "状态(1:未归 0 : 已归)")
	private Integer status;

	@ApiModelProperty(value = "外出时间")
	private Date outTime;

	@ApiModelProperty(value = "归来时间")
	private Date backTime;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

}
