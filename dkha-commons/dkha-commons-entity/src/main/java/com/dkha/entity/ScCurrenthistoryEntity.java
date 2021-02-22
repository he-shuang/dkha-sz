

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_currenthistory")
public class ScCurrenthistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 电流互感器采集ID
     */
	@TableId
	private Long chId;

    /**
     * 电流互感器采集设备编号
     */
	private String chDevicesn;

    /**
     * 采集设备ID
     */
	private Long chDeviceid;

    /**
     * 采集电流值
     */
	private Float chEcurrent;

    /**
     * 采集时间
     */
	private Date chColltime;

    /**
     * 房间编号
     */
	private String chNum;

    /**
     * 房间ID
     */
	private Long chRoomid;

	@TableField(exist = false)
	private Long floorid;
}
