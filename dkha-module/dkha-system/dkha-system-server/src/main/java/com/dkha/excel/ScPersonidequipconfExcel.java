package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 人证配置信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Data
public class ScPersonidequipconfExcel {
    @Excel(name = "配置信息ID")
    private Long pideId;
    @Excel(name = "设备编号")
    private String pieEquipsn;
    @Excel(name = "通过阀值：比如  70")
    private Integer thresholdvalue;
    @Excel(name = "是否开启单目活体")
    private Integer openmonocular;
    @Excel(name = "开启双目活体")
    private Integer openbioliveness;
    @Excel(name = "设备终端进入设置项的登录账号")
    private String adminaccount;
    @Excel(name = "设备终端进入设置项目的登录密码")
    private String adminpwd;
    @Excel(name = "是否可配")
    private Integer configurable;
    @Excel(name = "1.纯自定义文字播报2.纯姓名播报3 自定义文字+姓名播报4 姓名+自定义文字必报")
    private Integer ttsmodel;
    @Excel(name = "成功提示")
    private String passstr;
    @Excel(name = "失败提示")
    private String failedstr;
    @Excel(name = "设备上报baseURL")
    private String devicereportbaseurl;
    @Excel(name = "设备上报subURL")
    private String devicereportsuburl;
    @Excel(name = "人证认证记录baseUrL")
    private String cardrecordbaseurl;
    @Excel(name = "人证认证记录subURL")
    private String cardrecordsuburl;
    @Excel(name = "是否开启人证信息服务器校验")
    private Integer isidservervalidate;
    @Excel(name = "人证信息服务器校验URL")
    private Integer idservervalidateurl;
    @Excel(name = "最新版本号，如与设备端不同将开启升级流程")
    private String version;

}
