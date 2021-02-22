package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 *
 * @author Mark 
 * @since v1.0.0 2020-10-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_aidoorfive_personlist")
public class ScAidoorfivePersonlistEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId
	private Long apId;
    /**
     * 设备mq序列号
     */
	private String serial;
    /**
     * 设备mq密码
     */
	private String password;
    /**
     * 用户id
     */
	private String userId;
    /**
     * 照片在设备中的id
     */
	private String imgId;
    /**
     * 注册状态:1成功，0失败
     */
	private Integer status;
    /**
     * 人员姓名
     */
	private String username;
    /**
     * 用户编号
     */
	private String userno;
    /**
     * 学生性别：1男 0女
     */
	private Integer sex;
    /**
     * 照片地址
     */
	private String photoimg;
    /**
     * 照片地址
     */
	private String drNum;
    /**
     * 更新时间
     */
	private Date updateDate;
    /**
     * 记录创建时间
     */
	private Date downfaceTime;
    /**
     * 注册完成时间
     */
	private Date completeTime;
	/**
	 * 状态备注
	 */
	private String statusNote;
}
