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
 * 8英寸智能门禁设备具体的人脸信息
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-16
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_aidooreight_personlist")
public class ScAidooreightPersonlistEntity  implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId
	private Long apId;
    /**
     * 设备端ID
     */
	private Long aeId;
    /**
     * 设备SN号码
     */
	private String aeSerialnumber;
    /**
     * 人员姓名
     */
	private String username;
    /**
     * 用户ID
     */
	private Long userid;
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
     * 0 教师 1 保洁 2 保安   3 学生
     */
	private Integer persontype;
    /**
     *
     */
	private Date updateDate;
}
