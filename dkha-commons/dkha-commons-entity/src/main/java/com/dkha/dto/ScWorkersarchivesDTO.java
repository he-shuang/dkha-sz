package com.dkha.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * 教职工档案
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "教职工档案")
public class ScWorkersarchivesDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "职工档案ID")
	private Long scWaid;

	@ApiModelProperty(value = "职工姓名")
	private String scWaname;

	@ApiModelProperty(value = "性别：0男 1女")
	private Integer scWasex;

	@ApiModelProperty(value = "职工编号")
	private String scEmpno;

	@ApiModelProperty(value = "职工类型：0 教师 1 保洁 2 保安 ")
	private Integer scEmptype;

	@ApiModelProperty(value = "状态： -1 离职  0 正常 ")
	private Integer scStatus;

	@ApiModelProperty(value = "职工照片地址")
	private String scPhotoimg;

	@ApiModelProperty(value = "职工RFCardid编号")
	private String scRfcardid;

	@ApiModelProperty(value = "职工uwb编号")
	private String scUwbid;

	@ApiModelProperty(value = "职工手机号")
	private String scPhonenum;

	@ApiModelProperty(value = "职工入职时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private String scHiredate;

	@ApiModelProperty(value = "身份证号")
	private String scIdno;

	@ApiModelProperty(value = "创建人ID")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "修改人ID")
	private Long updater;

	@ApiModelProperty(value = "修改时间")
	private Date updateDate;

	@ApiModelProperty(value = "职工照片")
	private byte[] scPhotoimgByte;
}
