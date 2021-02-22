package com.dkha.dto.doorcontrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 门禁更新信息实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeviceSettingInfo {
   private String companyName;//	下标文字	String	N	允许为空；不超过30个字符
    private String devicePassword;//	管理密码	String	N	进入设置页的管理密码，不超过30个字符，由数字和字母组成，区分大小写
    private String deviceName;//	设备名称	String	N	允许为空；不超过30个字符
    private String   threshold ;//	人脸识别阈值	String	N	推荐0.8，0.00<= x <= 1.00，保留两位小数
    private String   openDelay;//	关门延时	String	N	0.0<= x <=100.0，保留一位小数
    private String  interval;//	识别失败重试间隔	String	N	1.0<= x <=10.0，保留一位小数
    private String   voiceMode;//	成功语音模式	String	N	1：不播报； 2：播报姓名； 3：预置音-识别成功； 4：预置音-欢迎光临； 5：预置音-门已打开； 6：预置音-门已打开，欢迎光临； 100：播报自定义内容
    private String   voiceCustom;//	成功语音自定义内容	String	N	允许为空；不超过30个字符
    private String  displayMode	;//成功显示模式	String	N	1：显示姓名； 2：隐藏姓名最后一个字符 100：显示自定义内容
    private String displayCustom;//	成功显示自定义内容	String	N	允许为空；不超过30个字符
    private String  strangerMode;//	失败显示模式	String	N	1：默认标识； 2：不反馈； 100：显示自定义内容
    private String   strangerCustom	;//失败显示自定义内容	String	N	允许为空；不超过30个字符
    private String   strangerVoiceMode;//	失败语音模式	String	N	1：不播报； 2：播报警报音； 3：预置音-识别失败； 4：预置音-验证不通过； 100：播报自定义内容
    private String   strangerVoiceCustom;//	失败语音自定义内容	String	N	允许为空；不超过30个字符
    private String   maxFaceSize;//	最大人脸检测数	Int	N	设备端暂未启用该参数
    private String    livenessType;//	活体检测类型	Int	N	0：关闭活体检测； 1：RGB； 2：IR
    private String  signDistance;//	识别距离	Int	N	1：很近，0 ~1米； 2：近，0~1.5米； 3：中等，0~2米； 4：较远，0~2.5米； 5：远，0~3.5米
    private String   successRetryDelay;//	识别成功重试间隔	String	N	1.0<= x <=10.0，保留一位小数
    private String   successRetry;//	识别成功重试开关	Int	N	1：打开；0：关闭
    private String   uploadRecordImage;//	识别记录图片上传开关	Int	N	1：打开；0：关闭
    private String  irLivePreview;//	IR活体预览显示开关	Int	N	1：打开；0：关闭
    private String  rebootEveryDay;//	设备每日重启开关	Int	N	1：打开；0：关闭
    private String   rebootHour;//	设备每日重启小时数值	String	N	范围：0~23
    private String   rebootMin	;//设备每日重启分钟数值	String	N	范围：0~59
    private String   faceQuality;//	人脸质量检测开关	Int	N	1：打开；0：关闭
    private String   faceQualityThreshold;//	人脸质量检测阈值	String	N	推荐0.35，0.00<= x <= 1.00，保留两位小数

}
