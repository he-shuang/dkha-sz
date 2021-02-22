package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 8英寸智能门禁设备每日采集数量
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_aidooreight_daily")
public class ScAidooreightDailyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 设备ID
     */
	private Long aeId;
    /**
     * 采集时间
     */
	private Date mdGatherdate;
    /**
     * 采集数量
     */
	private Long mdGathercount;
    /**
     * 更新时间
     */
	private Date updateDate;
}