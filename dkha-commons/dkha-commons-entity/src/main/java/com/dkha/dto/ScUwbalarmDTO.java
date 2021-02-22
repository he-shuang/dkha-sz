package com.dkha.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * uwb报警内容：工具标签报警，访客禁区报警，保密区域报警
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "uwb报警内容：工具标签报警，访客禁区报警，保密区域报警")
public class ScUwbalarmDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "uwb报警ID")
	private Long ubaId;

	@ApiModelProperty(value = "报警类型：0 工具标签报警  1 访客禁区报警 2 保密区域报警 ")
	private Integer ubaAlarmtype;

	@ApiModelProperty(value = "uwb报警标签编号")
	private String ubaUwbid;

	@ApiModelProperty(value = "uwb报警区域编号")
	private String ubaUwbregionno;

	@ApiModelProperty(value = "报警时间")
	private Date ubaAlarmtime;

	@ApiModelProperty(value = "报警是否已处理")
	private Integer ubaIshandler;

	@ApiModelProperty(value = "报警对应的实际位置")
	private String ubaAddress;

	/**
	 * 工牌名称
	 */
	private String uwbname;

}
