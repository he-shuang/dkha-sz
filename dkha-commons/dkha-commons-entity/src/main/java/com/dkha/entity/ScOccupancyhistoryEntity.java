

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 某房间的入住历史记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_occupancyhistory")
public class ScOccupancyhistoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 入住历史ID
     */
	@TableId
	private Long ohId;

    /**
     * 房间ID
     */
	private Long drId;

    /**
     * 入住学生姓名
     */
	private String stuname;

    /**
     * 入住学生学号
     */
	private String stuno;

    /**
     * 入住学生档案ID
     */
	private Long stuid;

    /**
     * 入住时间
     */
	private Date checkinDate;

    /**
     * 退房时间
     */
	private Date checkoutDate;

    /**
     * 创建人ID
     */
	private Long creator;
}
