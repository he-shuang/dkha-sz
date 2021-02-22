package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-16
 */
@Data
@ApiModel(value = "")
public class ScAidoorfivePersonlistDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long apId;

	@ApiModelProperty(value = "设备mq序列号")
	private String serial;

	@ApiModelProperty(value = "设备mq密码")
	private String password;

	@ApiModelProperty(value = "用户id")
	private String userId;

	@ApiModelProperty(value = "人员姓名")
	private String username;

	@ApiModelProperty(value = "用户id")
	private Long userid;

	@ApiModelProperty(value = "用户编号")
	private String userno;

	@ApiModelProperty(value = "学生性别：1男 0女")
	private Integer sex;

	@ApiModelProperty(value = "照片地址")
	private String photoimg;

	@ApiModelProperty(value = "")
	private Date updateDate;

}