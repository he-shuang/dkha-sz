package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("sc_aidoorfive_open_log")
public class ScAidoorfiveOpenLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
	private Long id;
    /**
     * 人脸图片相对路径
     */
	private String faceUrl;
    /**
     * 人员姓名
     */
	private String name;
    /**
     * 设备名称
     */
	private String deviceName;
    /**
     * 设备序列号
     */
	private String serialNumber;
    /**
     * 开门时间
     */
	private String addDate;
    /**
     * 人员ID
     */
	private String userId;
}
