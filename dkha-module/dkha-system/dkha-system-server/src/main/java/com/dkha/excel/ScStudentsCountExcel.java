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
public class ScStudentsCountExcel implements Serializable {
    private static final long serialVersionUID = 1L;

	@Excel(name = "学生姓名")
	private String scStuname;

	@ApiModelProperty(value = "学生照片地址")
	private String scPhotoimg;

	@Excel(name = "性别",replace = {"男_1","女_0"})
	private Integer scSex;

	@ApiModelProperty(value = "宿舍号")
	@Excel(name = "宿舍号")
	private String drNum;


	@ApiModelProperty(value = "学生手机号")
	@Excel(name = "学生手机号")
	private String scPhonenum;

	@ApiModelProperty(value = "未归次数")
	@Excel(name = "未归次数")
	private String number;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "学生档案ID")
	private Long scStdid;


	@Excel(name = "照片", type = 2, width = 15, height = 35, imageType = 3)
	private byte[] scPhotoimgByte;

}
