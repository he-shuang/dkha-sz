package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 门禁同行记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "门禁同行记录")
public class ScGateaccesscontrolDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "门禁开门ID")
	private Long gacId;

	@ApiModelProperty(value = "门禁设备编号")
	private String gacSn;

	@ApiModelProperty(value = "门禁方向：0 进  1 出")
	private Integer gacIotype;

	@ApiModelProperty(value = "开门时间")
	private Date gacOpentime;

	@ApiModelProperty(value = "开门人员姓名")
	private String gacName;

	@ApiModelProperty(value = "开门人员图像地址")
	private String gacPersonimg;

	@ApiModelProperty(value = "设备地址")
	private String gacDeviceadd;

	@ApiModelProperty(value = "通行人温度")
	private Float gacTemperature;

}
