package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 房间及房间状态信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "房间及房间状态信息")
public class ScDormitoryDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "房间ID")
	private Long drId;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "房间编号")
	private String drNum;

	@ApiModelProperty(value = "房间可入住人数")
	private Integer drCapacity;

	@ApiModelProperty(value = "房间状态：0停用，1未入住，2已入住")
	private Integer drState;

	@ApiModelProperty(value = "使用用途：0 学生宿舍  1 教师宿舍 2 教学楼")
	private Integer dfPurpose;

	@ApiModelProperty(value = "是否已住满：0 未住满，1 已住满")
	private Integer dfIsfull;

	@ApiModelProperty(value = "是否正在上传人脸：0 未上传，1 正在上传")
	private Integer isUpload;

	@ApiModelProperty(value = "当前下发人数")
	private String uploadNumber;

	@ApiModelProperty(value = "已经下发成功人数")
	private String uploadSuccessNumber;

}
