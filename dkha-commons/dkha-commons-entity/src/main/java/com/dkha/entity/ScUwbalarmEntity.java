

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * uwb报警内容：工具标签报警，访客禁区报警，保密区域报警
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_uwbalarm")
public class ScUwbalarmEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * uwb报警ID
     */
	@TableId
	private Long ubaId;

    /**
     * 报警类型：0 工具标签报警  1 访客禁区报警 2 保密区域报警
     */
	private Integer ubaAlarmtype;

    /**
     * uwb报警标签编号
     */
	private String ubaUwbid;

    /**
     * uwb报警区域编号
     */
	private String ubaUwbregionno;

    /**
     * 报警时间
     */
	private Date ubaAlarmtime;

    /**
     * 报警是否已处理
     */
	private Integer ubaIshandler;

    /**
     * 报警对应的实际位置
     */
	private String ubaAddress;

	/**
	 * 工牌名称
	 */
	@TableField(exist=false)
	private String uwbname;
}
