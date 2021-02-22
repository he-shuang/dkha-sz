package com.dkha.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
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
 * 学生档案信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "学生档案信息")
public class ScStudentsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "学生档案ID")
	private Long scStdid;

	@ApiModelProperty(value = "学生姓名")
	@Length(max = 50,message = "名称长度不能超过50个字符", groups = DefaultGroup.class)
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

	@ApiModelProperty(value = "创建人ID")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "修改人ID")
	private Long updater;

	@ApiModelProperty(value = "修改时间")
	private Date updateDate;

	@ApiModelProperty(value = "学生照片")
//	@Excel(name = "学生照片", type = 2, width = 25, height = 75, imageType = 3)
	private byte[] scPhotoimgByte;

	@ApiModelProperty(value = "学生")
	private  String scHeadphotoimg;

	@ApiModelProperty(value = "所属学院")
	private  String scSchool;

}
