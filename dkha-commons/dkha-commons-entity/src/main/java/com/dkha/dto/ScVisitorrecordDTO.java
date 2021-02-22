package com.dkha.dto;

import com.dkha.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 访客记录表
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "访客记录")
public class ScVisitorrecordDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "访客记录Id ")
	private Long vrId;

	@ApiModelProperty(value = "访客姓名")
	@NotBlank(message="访客姓名不能为空", groups = DefaultGroup.class)
	private String vrName;

	@ApiModelProperty(value = "访客电话")
	private String vrPhone;

	@ApiModelProperty(value = "性别：0 男 1女")
	@Range(min=0, max=1, message = "性别参数格式有误", groups = DefaultGroup.class)
	private Integer vrSex;

	@ApiModelProperty(value = "访客证件身份证号码")
	@NotBlank(message="访客证件身份证号码不能为空", groups = DefaultGroup.class)
	private String vrIdno;

	@ApiModelProperty(value = "访客照片地址")
	private String vrPhoneimg;

	@ApiModelProperty(value = "访客开始时间")
	private Date vrVistorbegintime;

	@ApiModelProperty(value = "被访人电话")
	private String vrInterviewedPhone;

	@ApiModelProperty(value = "被访人ID")
	@NotBlank(message="被访人姓名不能为空", groups = DefaultGroup.class)
	private String vrInterviewed;

	@ApiModelProperty(value = "被访人姓名")
	private String vrInterviewedName;

	@ApiModelProperty(value = "访问事由")
	private String vrReasons;

	@ApiModelProperty(value = "访问uwb编号")
	@NotBlank(message="访问uwb编号不能为空", groups = DefaultGroup.class)
	private String vrUwbid;

	@ApiModelProperty(value = "访客结束时间")
	private Date vrVistorendtime;

	@ApiModelProperty(value = "是否归还uwb工牌:0 未归还，1 归还")
	private Integer vrReturnuwb;

	@ApiModelProperty(value = "访客温度")
	@NotNull(message="访客温度不能为空", groups = DefaultGroup.class)
	private BigDecimal vrTemperature;

	@ApiModelProperty(value = "被访人地址信息")
	@NotBlank(message="被访人地址信息不能为空", groups = DefaultGroup.class)
	private String vrAddress;

	@ApiModelProperty(value = "创建人ID")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "修改人ID")
	private Long updater;

	@ApiModelProperty(value = "修改时间")
	private Date updateDate;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "照片")
	private byte[] scPhotoimgByte;
	@ApiModelProperty(value = "楼层名称")
	private String floorname;

	@ApiModelProperty(value = "楼栋名称")
	private String parentfloorname;


	private int num;

}
