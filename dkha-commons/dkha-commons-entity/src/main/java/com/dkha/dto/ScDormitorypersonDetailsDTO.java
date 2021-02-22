package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 宿舍当前入住人员信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "宿舍当前入住人员信息")
public class ScDormitorypersonDetailsDTO extends ScStudentsDTO implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "入住ID")
	private Long dpId;

	@ApiModelProperty(value = "房间ID")
	private Long drId;

	@ApiModelProperty(value = "学生档案ID")
	private Long scStdid;

	@ApiModelProperty(value = "入住时间")
	private Date drOccupancydate;

	@ApiModelProperty(value = "是否外出(1:是 外出  0: 否 归寝)")
	private Integer isOut;





}
