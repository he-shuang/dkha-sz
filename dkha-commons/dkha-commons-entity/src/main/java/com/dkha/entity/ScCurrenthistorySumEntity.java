package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since v1.0.0 2020-11-06
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_currenthistory_sum")
public class ScCurrenthistorySumEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
	private Long id;
    /**
     * 房间编号
     */
	private String chNum;
    /**
     * 房间id
     */
	private Long chRoomid;
    /**
     * 电流值
     */
	private Float chEcurrent;
    /**
     * 楼层id
     */
	private Long chFloorid;
    /**
     * 采集时间
     */
	private Date chColltime;
    @TableField(exist = false)
    private Long floorid;
}
