package com.dkha.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人证配置信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_personidequipconf")
public class ScPersonidequipconfEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 配置信息ID
     */
	@TableId
	private Long pideId;
    /**
     * 设备编号
     */
	private String pieEquipsn;
    /**
     * 通过阀值：比如  70
     */
	private Integer thresholdvalue;
    /**
     * 是否开启单目活体
     */
	private Boolean openmonocular;
    /**
     * 开启双目活体
     */
	private Boolean openbioliveness;
    /**
     * 设备终端进入设置项的登录账号
     */
	private String adminaccount;
    /**
     * 设备终端进入设置项目的登录密码
     */
	private String adminpwd;
    /**
     * 是否可配
     */
	private Boolean configurable;
    /**
     * 1.纯自定义文字播报
            2.纯姓名播报
            3 自定义文字+姓名播报
            4 姓名+自定义文字必报
     */
	private Integer ttsmodel;
    /**
     * 成功提示
     */
	private String passstr;
    /**
     * 失败提示
     */
	private String failedstr;
    /**
     * 设备上报baseURL
     */
	private String devicereportbaseurl;
    /**
     * 设备上报subURL
     */
	private String devicereportsuburl;
    /**
     * 人证认证记录baseUrL
     */
	private String cardrecordbaseurl;
    /**
     * 人证认证记录subURL
     */
	private String cardrecordsuburl;
    /**
     * 是否开启人证信息服务器校验
     */
	private Boolean isidservervalidate;
    /**
     * 人证信息服务器校验URL
     */
	private Boolean idservervalidateurl;
    /**
     * 最新版本号，如与设备端不同将开启升级流程
     */
	private String version;
}
