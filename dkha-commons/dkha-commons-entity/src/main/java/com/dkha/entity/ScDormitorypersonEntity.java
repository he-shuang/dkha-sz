

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 宿舍当前入住人员信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_dormitoryperson")
public class ScDormitorypersonEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 入住ID
     */
	@TableId
	private Long dpId;

    /**
     * 房间ID
     */
	private Long drId;

    /**
     * 学生档案ID
     */
	private Long scStdid;

    /**
     * 入住时间
     */
	private Date drOccupancydate;

	/**
	 * 是否外出(1:是 外出  0: 否 归寝)
	 */
	private Integer isOut;
	/**
	 * 是否外出(1:是 外出  0: 否 归寝)
	 */
	private Integer isUwbout;

	/**
	 * 房间号码
	 */
	@TableField(exist = false)
	private String drNum;
}
