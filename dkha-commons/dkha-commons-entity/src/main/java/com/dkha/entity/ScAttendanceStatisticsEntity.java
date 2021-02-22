package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 考勤统计
 *
 * @author Mark 
 * @since v1.0.0 2020-12-14
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_attendance_statistics")
public class ScAttendanceStatisticsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
	private Long id;
    /**
     * 房间名称(8寸门禁设备名称)
     */
	private String aeDevicename;
    /**
     * 8寸门禁设备ID
     */
	private Long aeId;
    /**
     * 职工ID
     */
	private Long scWaid;
    /**
     * 职工姓名
     */
	private String scWaname;
    /**
     * 统计日期
     */
	private Date stDate;
    /**
     * 次数
     */
	private Integer stNum;
}
