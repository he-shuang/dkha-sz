
package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_aidooreightversion")
public class ScAidooreightversionEntity {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
	private Long aevId;
    /**
     * 程序包名称
     */
	private String aevPackname;
    /**
     * 程序版本号
     */
	private String aevVersion;
    /**
     * 程序版本code
     */
	private Integer aevVersioncode;
    /**
     * 本次程序版本说明
     */
	private String aevNote;


    /**
     * 成功升级设备数量
     */
	private Integer updatetotal;
    /**
     * 最后升级成功时间
     */
	private Date lastUpdatetime;
    /**
     * 升级包存储位置
     */
	private String aevUpdatefilepath;
	/**
	 * 创建时间
	 */
	private  Date createDate;
	/**
	 * 设备主板芯片：设备主板类型：0 F3 1 FR 2 FR&TM
	 */
	private Integer aeMainboard;
}