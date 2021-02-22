
package com.dkha.dto.doorcontrol;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-21
 */
@Data
@ApiModel(value = "")
public class ScAidooreightversionDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long aevId;

	@ApiModelProperty(value = "程序包名称")
	private String aevPackname;

	@ApiModelProperty(value = "程序版本号")
	private String aevVersion;

	@ApiModelProperty(value = "程序版本code")
	private Integer aevVersioncode;

	@ApiModelProperty(value = "本次程序版本说明")
	private String aevNote;

	@ApiModelProperty(value = "创建日期")
	private Date createDate;

	@ApiModelProperty(value = "成功升级设备数量")
	private Integer updatetotal;

	@ApiModelProperty(value = "最后升级成功时间")
	private Date lastUpdatetime;

	@ApiModelProperty(value = "升级包存储位置")
	private String aevUpdatefilepath;

	@ApiModelProperty(value = "设备主板芯片：设备主板类型：0 F3 1 FR 2 FR&TM")
	private Integer aeMainboard;

}