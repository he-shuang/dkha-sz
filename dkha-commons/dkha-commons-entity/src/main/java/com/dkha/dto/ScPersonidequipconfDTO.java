

package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;


/**
 * 人证配置信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Data
@ApiModel(value = "人证配置信息")
public class ScPersonidequipconfDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "配置信息ID")
	private Long pideId;

	@ApiModelProperty(value = "设备编号")
	private String pieEquipsn;

	@ApiModelProperty(value = "通过阀值：比如  70")
	private Integer thresholdvalue;

	@ApiModelProperty(value = "是否开启单目活体")
	private Boolean openmonocular;

	@ApiModelProperty(value = "开启双目活体")
	private Boolean openbioliveness;

	@ApiModelProperty(value = "设备终端进入设置项的登录账号")
	private String adminaccount;

	@ApiModelProperty(value = "设备终端进入设置项目的登录密码")
	private String adminpwd;

	@ApiModelProperty(value = "是否可配")
	private Boolean configurable;

	@ApiModelProperty(value = "1.纯自定义文字播报2.纯姓名播报3 自定义文字+姓名播报4 姓名+自定义文字必报")
	private Integer ttsmodel;

	@ApiModelProperty(value = "成功提示")
	private String passstr;

	@ApiModelProperty(value = "失败提示")
	private String failedstr;

	@ApiModelProperty(value = "设备上报baseURL")
	private String devicereportbaseurl;

	@ApiModelProperty(value = "设备上报subURL")
	private String devicereportsuburl;

	@ApiModelProperty(value = "人证认证记录baseUrL")
	private String cardrecordbaseurl;

	@ApiModelProperty(value = "人证认证记录subURL")
	private String cardrecordsuburl;

	@ApiModelProperty(value = "是否开启人证信息服务器校验")
	private Boolean isidservervalidate;

	@ApiModelProperty(value = "人证信息服务器校验URL")
	private Boolean idservervalidateurl;

	@ApiModelProperty(value = "最新版本号，如与设备端不同将开启升级流程")
	private String version;

}
