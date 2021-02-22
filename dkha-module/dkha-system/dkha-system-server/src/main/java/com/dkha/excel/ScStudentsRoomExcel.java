package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 学生档案信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "学生档案信息")
public class ScStudentsRoomExcel implements Serializable {
    private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "宿舍号")
	@Excel(name = "宿舍号")
	private String drNum;


	@ApiModelProperty(value = "未归次数")
	@Excel(name = "未归次数")
	private String number;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;


}
