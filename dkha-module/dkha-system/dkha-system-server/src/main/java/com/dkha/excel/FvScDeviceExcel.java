package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 设备表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Data
public class FvScDeviceExcel {
    @Excel(name = "")
    private Integer fId;
    @Excel(name = "代理商ID")
    private Integer fAgentId;
    @Excel(name = "设备名称")
    private String fName;
    @Excel(name = "添加时间")
    private Integer fAddTime;
    @Excel(name = "社区ID")
    private Integer fCommunityId;
    @Excel(name = "楼栋ID")
    private Integer fBuildingId;
    @Excel(name = "通道ID")
    private Integer fChannelId;
    @Excel(name = "扩展参数设置,1、活体检测,2、实时上传,3、关闭信息框,4、补光常亮,5、不记录照片,6、不上传照片,7、二维码识别,8、识别模式,9、单人/多人,10、4G  排列顺序：0,0,0,0,0,0,0,0,0,0")
    private String fParamsSet;
    @Excel(name = "开门状态,0-进，1-出")
    private Integer fUnlockState;
    @Excel(name = "开门模式,0-延时模式，1-常开模式,2-常闭模式")
    private Integer fLockMode;
    @Excel(name = "开门延时,0-0.5s,1-1s,2-3s,3-10s,4-20s")
    private Integer fLockDelay;
    @Excel(name = "mqtt序列号")
    private String fSerial;
    @Excel(name = "mqtt设备密码")
    private String fPassword;
    @Excel(name = "设备序列号")
    private String fSerialNumber;
    @Excel(name = "是否支持人脸")
    private Integer fSupportFace;
    @Excel(name = "设备序号")
    private Integer fDeviceNo;
    @Excel(name = "蓝牙key类型")
    private Integer fBtkeyType;
    @Excel(name = "蓝牙key")
    private String fBtkey;
    @Excel(name = "mac地址")
    private String fMac;
    @Excel(name = "")
    private Integer fWanDhcp;
    @Excel(name = "")
    private String fWanIp;
    @Excel(name = "")
    private String fWanMask;
    @Excel(name = "")
    private String fWanGateway;
    @Excel(name = "")
    private String fWifiSsid;
    @Excel(name = "")
    private String fWifiPwd;
    @Excel(name = "设备类型，0-读头，1-门禁")
    private Integer fDeviceType;
    @Excel(name = "机器平台,0-3288 1-520 2-其它")
    private Integer fAlgorithm;

}