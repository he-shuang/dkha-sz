package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 楼层与星网地图绑定表
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0 2020-08-29
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_dfmapbinding")
public class ScDfmapbindingEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId
	private Long id;
    /**
     * 楼层ID
     */
	private Long dfFloorid;
    /**
     * 地图楼栋ID
     */
	private Long mapId;
    /**
     * 地图楼层
     */
	private String floor;
}