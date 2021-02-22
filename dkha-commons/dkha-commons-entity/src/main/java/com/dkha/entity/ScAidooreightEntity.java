package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 智能设备主要分为：8英寸智能门禁设备
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_aidooreight")
public class ScAidooreightEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */

    @TableId(type = IdType.AUTO)
	private Long aeId;
    /**
     * 设备端IP
     */
	private String aeClientip;
    /**
     * 设备端端口
     */
	private Integer aeClientport;
    /**
     * 设备Mac地址
     */
	private String aeMacaddress;
    /**
     * 设备SN号
     */
	private String aeSerialnumber;
    /**
     * 设备名称
     */
	private String aeDevicename;
    /**
     * 设备签到类型：1签到，2 签退，3 签到和签退
     */
	private Integer aeSigntype;
    /**
     * 签名密钥: 用于签名sign ,初步设计为使用   （MAC地址+ DKHA字符 ）  进行MD5摘要后的字符串来作为签名 （界面不显示）
     */
	private String aeSignkey;
    /**
     * 管理端分配给设备端的唯一标识，设备端在回调管理端接口时会返回此信息，可作为判断设备端的依据
     */
	private String aeDeviceid;

	public  String getAeDeviceid(){
		return  aeId == null ? "" : aeId.toString();
	}
    /**
     *
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date aeSetupdate;
    /**
     * 设备有效期到期时间
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date aeExpirydate;
    /**
     *
     */
	private String aeSetupaddr;
    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
	private Integer aeState;
    /**
     *
     */
	private String remark;
    /**
     * 楼层ID
     */
	private String dfFloorid;

	@TableField(exist = false)
	private String floorName;

	/**
	 * 心跳最新时间戳单位秒(定时设备状态)
	 */
	private Long aeDatetime;
	/**
	 * 版本名称
	 */
	private String aeVersionname;
	/**
	 * 版本号
	 */
	private String aeVersioncode;
	/**
	 * 包名
	 */
	private String aePackagename;
	/**
	 * 设备下发人脸数
	 */
	private Integer aeFacetotal;
	/**
	 * 设备最新下发时间
	 */
	private Date aeFacedowntime;
    /**
     * 下发传输状态： 1正在传输 0 已完成
     */
	private Integer aeTransstate;
    /**
     * 设备类型：0 普通门禁，1,教学楼访客门禁  2教学楼门禁闸机 3.宿舍门禁闸机'
     */
	private Integer aeDevicetype;
	/**
	 * 设备主板芯片：设备主板类型：0 F3 1 FR 2 FR&TM
	 */
	private Integer aeMainboard;
}
