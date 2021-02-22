package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 8英寸智能门禁设备具体的人脸信息
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-16
 */
@Data
@ApiModel(value = "8英寸智能门禁设备具体的人脸信息")
public class ScAidooreightPersonlistDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long apId;

	@ApiModelProperty(value = "设备端ID")
	private Long aeId;

	@ApiModelProperty(value = "设备SN号码")
	private String aeSerialnumber;

	@ApiModelProperty(value = "人员姓名")
	private String username;

	@ApiModelProperty(value = "用户ID")
	private Long userid;

	@ApiModelProperty(value = "用户编号")
	private String userno;

	@ApiModelProperty(value = "学生性别：1男 0女")
	private Integer sex;

	@ApiModelProperty(value = "照片地址")
	private String photoimg;

	@ApiModelProperty(value = "0 教师 1 保洁 2 保安   3 学生")
	private Integer persontype;

	@ApiModelProperty(value = "")
	private Date updateDate;

}
