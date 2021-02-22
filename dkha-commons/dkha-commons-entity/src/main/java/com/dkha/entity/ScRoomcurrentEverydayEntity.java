package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 宿舍电流每日报警记录
 *
 * @author Mark
 * @since v1.0.0 2020-10-10
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_roomcurrent_everyday")
public class ScRoomcurrentEverydayEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId
	private Long id;
    /**
     * 房间编号
     */
	private String drNum;
    /**
     * 报警时间
     */
	private Date alarmDate;
    /**
     * 报警次数
     */
	private Integer num;
    /**
     * 更新时间
     */
	private Date updateDate;
    /**
     * 房间主键ID
     */
	private Long drRoomid;
}
