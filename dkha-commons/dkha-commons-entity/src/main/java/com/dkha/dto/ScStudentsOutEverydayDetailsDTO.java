package com.dkha.dto;

import com.dkha.commons.tools.validator.group.DefaultGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * 每日学生未归详情
 *
 * @author Mark 
 * @since v1.0.0 2020-10-15
 */
@Data
@ApiModel(value = "每日学生未归详情")
public class ScStudentsOutEverydayDetailsDTO  implements Serializable{
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Long id;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;


	@ApiModelProperty(value = "学生档案ID")
	private Long scStdid;

	@ApiModelProperty(value = "学生姓名")
	private String scStuname;

	@ApiModelProperty(value = "学生性别：0 男 1女")
	private Integer scSex;

	@ApiModelProperty(value = "学生学号")
	private String scNo;

	@ApiModelProperty(value = "学生身份证号")
	private String scIdno;

	@ApiModelProperty(value = "学生入学时间")
	private String scRegisterdate;

	@ApiModelProperty(value = "学生状态：0毕业，1在读,2肄业3,结业")
	private Integer scStatus;

	@ApiModelProperty(value = "学生学历：0本科，1硕士，2博士，3博士后")
	private Integer scEducation;

	@ApiModelProperty(value = "学生照片地址")
	private String scPhotoimg;

	@ApiModelProperty(value = "学生RFCardid编号")
	private String scRfcardid;

	@ApiModelProperty(value = "学生uwb编号")
	private String scUwbid;

	@ApiModelProperty(value = "学生手机号")
	private String scPhonenum;

	@ApiModelProperty(value = "毕业时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String scGraduationdate;

}