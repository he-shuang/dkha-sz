package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 5寸门禁设备
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("fv_sc_device")
public class FvScDeviceEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
	private Integer fId;
    /**
     * 代理商ID
     */
	private Integer fAgentId;
    /**
     * 设备名称
     */
	private String fName;
    /**
     * 添加时间
     */
	private Integer fAddTime;
    /**
     * 社区ID
     */
	private Integer fCommunityId;
    /**
     * 楼栋ID
     */
	private Integer fBuildingId;
    /**
     * 通道ID
     */
	private Integer fChannelId;
    /**
     * 扩展参数设置,1、活体检测,2、实时上传,3、关闭信息框,4、补光常亮,5、不记录照片,6、不上传照片,7、二维码识别,8、识别模式,9、单人/多人,10、4G  排列顺序：0,0,0,0,0,0,0,0,0,0
     */
	private String fParamsSet;
    /**
     * 开门状态,0-进，1-出
     */
	private Integer fUnlockState;
    /**
     * 开门模式,0-延时模式，1-常开模式,2-常闭模式
     */
	private Integer fLockMode;
    /**
     * 开门延时,0-0.5s,1-1s,2-3s,3-10s,4-20s
     */
	private Integer fLockDelay;
    /**
     * mqtt序列号
     */
	private String fSerial;
    /**
     * mqtt设备密码
     */
	private String fPassword;
    /**
     * 设备序列号
     */
	private String fSerialNumber;
    /**
     * 是否支持人脸
     */
	private Integer fSupportFace;
    /**
     * 设备序号
     */
	private Integer fDeviceNo;
    /**
     * 蓝牙key类型
     */
	private Integer fBtkeyType;
    /**
     * 蓝牙key
     */
	private String fBtkey;
    /**
     * mac地址
     */
	private String fMac;
    /**
     * 
     */
	private Integer fWanDhcp;
    /**
     * 
     */
	private String fWanIp;
    /**
     * 
     */
	private String fWanMask;
    /**
     * 
     */
	private String fWanGateway;
    /**
     * 
     */
	private String fWifiSsid;
    /**
     * 
     */
	private String fWifiPwd;
    /**
     * 设备类型，0-读头，1-门禁
     */
	private Integer fDeviceType;
    /**
     * 机器平台,0-3288 1-520 2-其它
     */
	private Integer fAlgorithm;
	/**
	 * 社区名称
	 */
	@TableField(exist = false)
	private String communityName;
	/**
	 * 楼栋名称
	 */
	@TableField(exist = false)
	private String buildingName;
    /**
     * 下发人数
     */
    @TableField(exist = false)
    private Integer numberPer;
    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
//    @TableField(exist = false)
    private Integer fState;

	/**
	 * 添加时间
	 */
	@TableField(exist = false)
	private Date fAddDate;
	@TableField(exist = false)
	private Integer fFaceTotal;
}