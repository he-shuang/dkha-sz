

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 电流互感器房间电流信息报警
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_transformalarm")
public class ScTransformalarmEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 电流互感器报警ID
     */
	@TableId
	private Long tfaId;

    /**
     * 互感器设备编号
     */
	private String tfDevicesn;

    /**
     * 互感器设备ID
     */
	private Long tfId;

    /**
     * 报警电流值
     */
	private Float tfaEcurrent;

    /**
     * 报警时间
     */
	private Date tfaAlarmtime;

    /**
     * 房间编号
     */
	private String drNum;

    /**
     * 房间ID
     */
	private Long drRoomid;

	/**
	 * 房间楼栋名称+房间编号 组合信息
	 */
	@TableField(exist=false)
	private String roomName;
	/**
	 * 报警是否已处理
	 */
	private Integer tfaIshandle;
	/**
	 * 报警处理时间
	 */
	private Date tfaHandletime;

}
