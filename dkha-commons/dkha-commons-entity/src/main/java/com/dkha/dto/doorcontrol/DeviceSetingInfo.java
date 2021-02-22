package com.dkha.dto.doorcontrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备端设置参数
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceSetingInfo {
    private String aeId; //设备主键ID
    private   String companyName;//下标文字	String
    private String devicePassword;//	设备密码	String
    private String deviceName;//	设备名称	String
    private  String threshold;//	识别阈值	String
    private String  openDelay;//	开门延迟	String
    private  String interval;//	识别失败重试间隔	String
    private String  voiceMode;//	语音模式	Int
    private String  voiceCustom	;//语音自定义内容	String
    private String displayMode	;//显示模式	Int
    private String  displayCustom;//	显示自定义内容	String
    private  String strangerMode;//	陌生人模式	Int
    private String strangerCustom;//	陌生人自定义显示	String
    private  String strangerVoiceMode;//	陌生人语音模式	Int
    private  String strangerVoiceCustom	;//陌生人语音自定义内容	String
    private  int maxFaceSize;//	最大人脸检测数	Int
    private int livenessType;//	活体类型	Int
    private  int signDistance;//	识别距离	Int
    private String  successRetryDelay;//	识别成功重试时间	String
    private int  successRetry;//	识别成功重试开关	Int
    private int  uploadRecordImage;//	识别记录图片上传开关	Int
    private  int irLivePreview;//	IR活体预览显示开关	Int
    private int rebootEveryDay;//	设备每日重启开关	Int
    private String rebootHour;//	设备每日重启小时值	String
    private String  rebootMin;//	设备每日重启分钟值	String
    private int versionCode;//	版本号	Int
    private String  packageName;//	包名	String
    private String versionName;//	版本名称	String
    private int  faceQuality;//	人脸质量检测开关	Int
    private String faceQualityThreshold;//	人脸质量检测阈值	String
    private Boolean isOpenTemp; //是否打开测温
    private Boolean isOpenRadar; //是否打开自动暗屏
    private String bottomTitle; //底部右下角标题
}
