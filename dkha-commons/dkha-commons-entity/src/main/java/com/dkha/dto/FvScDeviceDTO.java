package com.dkha.dto;

import com.dkha.entity.ScAidoorfivePersonlistEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;


/**
 * 5寸门禁设备
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Data
@ApiModel(value = "5寸门禁设备")
public class FvScDeviceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "")
	private Integer fId;

	@ApiModelProperty(value = "代理商ID")
	private Integer fAgentId;

	@ApiModelProperty(value = "设备名称")
	private String fName;

	@ApiModelProperty(value = "添加时间戳")
	private Integer fAddTime;

	@ApiModelProperty(value = "社区ID")
	private Integer fCommunityId;

	@ApiModelProperty(value = "楼栋ID")
	private Integer fBuildingId;

	@ApiModelProperty(value = "通道ID")
	private Integer fChannelId;

	@ApiModelProperty(value = "扩展参数设置,1、活体检测,2、实时上传,3、关闭信息框,4、补光常亮,5、不记录照片,6、不上传照片,7、二维码识别,8、识别模式,9、单人/多人,10、4G  排列顺序：0,0,0,0,0,0,0,0,0,0")
	private String fParamsSet;

	@ApiModelProperty(value = "开门状态,0-进，1-出")
	private Integer fUnlockState;

	@ApiModelProperty(value = "开门模式,0-延时模式，1-常开模式,2-常闭模式")
	private Integer fLockMode;

	@ApiModelProperty(value = "开门延时,0-0.5s,1-1s,2-3s,3-10s,4-20s")
	private Integer fLockDelay;

	@ApiModelProperty(value = "mqtt序列号")
	private String fSerial;

	@ApiModelProperty(value = "mqtt设备密码")
	private String fPassword;

	@ApiModelProperty(value = "设备序列号")
	private String fSerialNumber;

	@ApiModelProperty(value = "是否支持人脸")
	private Integer fSupportFace;

	@ApiModelProperty(value = "设备序号")
	private Integer fDeviceNo;

	@ApiModelProperty(value = "蓝牙key类型")
	private Integer fBtkeyType;

	@ApiModelProperty(value = "蓝牙key")
	private String fBtkey;

	@ApiModelProperty(value = "mac地址")
	private String fMac;

	@ApiModelProperty(value = "")
	private Integer fWanDhcp;

	@ApiModelProperty(value = "")
	private String fWanIp;

	@ApiModelProperty(value = "")
	private String fWanMask;

	@ApiModelProperty(value = "")
	private String fWanGateway;

	@ApiModelProperty(value = "")
	private String fWifiSsid;

	@ApiModelProperty(value = "")
	private String fWifiPwd;

	@ApiModelProperty(value = "设备类型，0-读头，1-门禁")
	private Integer fDeviceType;

	@ApiModelProperty(value = "机器平台,0-3288 1-520 2-其它")
	private Integer fAlgorithm;

	@ApiModelProperty(value = "社区名称")
	private String communityName;

	@ApiModelProperty(value = "楼栋名称")
	private String buildingName;

    @ApiModelProperty(value = "下发人数")
    private Integer numberPer;

    @ApiModelProperty(value = "设备状态：-1 离线 0 正常 1 停用")
    private Integer fState;

	@ApiModelProperty(value = "添加时间")
	private Date fAddDate;

	@ApiModelProperty(value = "设备下发人脸数")
	private Integer fFaceTotal;

	List<ScAidoorfivePersonlistEntity> personList;
}