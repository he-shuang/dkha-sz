package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 
 *
 * @author dkha 
 * @since v1.0.0 2020-10-22
 */
@Data
@ApiModel(value = "")
public class ScStudentsInandoutDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "学生出进记录次数id")
	private Long id;

	@ApiModelProperty(value = "学生档案ID")
	private Long scStdid;

	@ApiModelProperty(value = "进入次数")
	private String inSum;

	@ApiModelProperty(value = "出门次数")
	private String outSum;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;
	/**
	 * 类型： 1宿舍 2教学楼
	 */
	private int type;

}