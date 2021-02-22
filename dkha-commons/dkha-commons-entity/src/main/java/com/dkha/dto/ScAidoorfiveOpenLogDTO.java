package com.dkha.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-16
 */
@Data
@ApiModel(value = "五寸门禁系统开门记录信息")
public class ScAidoorfiveOpenLogDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long id;

	@ApiModelProperty(value = "人脸图片ID")
	private String faceUrl;

	@ApiModelProperty(value = "人员姓名")
	private String name;

	@ApiModelProperty(value = "设备名称")
	private String deviceName;

	@ApiModelProperty(value = "设备序列号")
	private String serialNumber;

	@ApiModelProperty(value = "开门时间")
	private String addDate;

	@ApiModelProperty(value = "学生ID")
	private String userId;
}
