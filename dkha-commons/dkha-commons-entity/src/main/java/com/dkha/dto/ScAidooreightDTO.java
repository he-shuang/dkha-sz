
package com.dkha.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * 智能设备主要分为：8英寸智能门禁设备
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-14
 */
@Data
@ApiModel(value = "智能设备主要分为：8英寸智能门禁设备")
public class ScAidooreightDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	private Long aeId;

	@ApiModelProperty(value = "设备端IP")
	private String aeClientip;

	@ApiModelProperty(value = "设备端端口")
	private Integer aeClientport;

	@ApiModelProperty(value = "设备Mac地址")
	private String aeMacaddress;

	@ApiModelProperty(value = "设备SN号")
	private String aeSerialnumber;

	@ApiModelProperty(value = "设备名称")
	private String aeDevicename;

	@ApiModelProperty(value = "设备签到类型：1签到，2 签退，3 签到和签退")
	private Integer aeSigntype;

	@ApiModelProperty(value = "签名密钥: 用于签名sign ,初步设计为使用   （MAC地址+ DKHA字符 ）  进行MD5摘要后的字符串来作为签名 （界面不显示）")
	private String aeSignkey;

	@ApiModelProperty(value = "管理端分配给设备端的唯一标识，设备端在回调管理端接口时会返回此信息，可作为判断设备端的依据")
	private String aeDeviceid;

	@ApiModelProperty(value = "设备安装时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date aeSetupdate;

	@ApiModelProperty(value = "设备有效期到期时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date aeExpirydate;

	@ApiModelProperty(value = "")
	private String aeSetupaddr;

	@ApiModelProperty(value = "设备状态：-1 离线 0 正常 1 停用")
	private Integer aeState;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "楼层ID")
	private String dfFloorid;

	@ApiModelProperty(value = "楼层名称")
	private String floorName;

	@ApiModelProperty(value = "心跳最新时间戳单位秒(定时设备状态)")
	private Long aeDatetime;

	@ApiModelProperty(value = "版本名称")
	private String aeVersionname;

	@ApiModelProperty(value = "版本号")
	private String aeVersioncode;

	@ApiModelProperty(value = "包名")
	private String aePackagename;

	@ApiModelProperty(value = "设备下发人脸数")
	private Integer aeFacetotal;

	@ApiModelProperty(value = "设备最新下发时间")
	private Date aeFacedowntime;

	@ApiModelProperty(value = "下发传输状态 1正在传输 0 已完成")
	private Integer aeTransstate;

	@ApiModelProperty(value = "设备类型 0 正常 1 访客")
	private Integer aeDevicetype;
	@ApiModelProperty(value = "设备主板芯片：设备主板类型：0 F3 1 FR 2 FR&TM")
	private Integer aeMainboard;
	@ApiModelProperty(value = "部门ID列表")
	private List<Long> deptIdList;

}