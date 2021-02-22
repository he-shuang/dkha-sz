package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 5寸门禁开门记录
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class FvScDoorRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
	private Integer fId;
    /**
     * 添加时间
     */
	private Integer fAddTime;
    /**
     * 权限ID
     */
	private String fKeyId;
    /**
     * 设备ID
     */
	private Integer fDeviceId;
    /**
     * 开门类型,1- 卡号 2- 注册用户id   3-蓝牙发的卡号（手机号） 4-密码开门   5-指纹ID  6-人脸ID
     */
	private Integer fOpenType;
    /**
     * 通过时间
     */
	private Integer fOpenTime;
    /**
     * 合法标志, 0-非法 1-合法
     */
	private Integer fState;
    /**
     * 人员ID
     */
	private Integer fPersonId;
    /**
     * 邀请人
     */
	private String fInviterName;
    /**
     * 代理商
     */
	private Integer fAgentId;
	/**
	 * 下发人脸地址
	 */
	@TableField(exist = false)
	private String fFaceUrl;
	/**
	 * 添加时间
	 */
	@TableField(exist = false)
	private Date fAddDate;
	/**
	 * 员工名称
	 */
	@TableField(exist = false)
	private String fName;

	/**
	 * 员设备名称
	 */
	@TableField(exist = false)
	private String deviceName;

	/**
	 * 设备编号
	 */
	@TableField(exist = false)
	private String fSerialNumber;
}