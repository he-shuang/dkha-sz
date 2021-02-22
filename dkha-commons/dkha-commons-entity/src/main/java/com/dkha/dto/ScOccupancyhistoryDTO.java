package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 某房间的入住历史记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "某房间的入住历史记录")
public class ScOccupancyhistoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "入住历史ID")
	private Long ohId;

	@ApiModelProperty(value = "房间ID")
	private Long drId;

	@ApiModelProperty(value = "入住学生姓名")
	private String stuname;

	@ApiModelProperty(value = "入住学生学号")
	private String stuno;

	@ApiModelProperty(value = "入住学生档案ID")
	private Long stuid;

	@ApiModelProperty(value = "入住时间")
	private Date checkinDate;

	@ApiModelProperty(value = "退房时间")
	private Date checkoutDate;

	@ApiModelProperty(value = "创建人ID")
	private Long creator;

}
