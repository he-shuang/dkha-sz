package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 区域配置/uwb围栏关联
 *
 * @author Mark
 * @since v1.0.0 2020-09-01
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_region_config")
public class ScRegionConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
	private Long rcId;
    /**
     * 区域名称
     */
	private String rcName;
    /**
     * 区域编号
     */
	private String rcNo;
    /**
     * 区域地址/位置
     */
	private String rcAddress;
    /**
     * 区域楼栋/楼层(两级)
     */
	private String rcFloor;
	@TableField(exist = false)
	private String rcFloorName;
    /**
     * uwb围栏ID
     */
	private Long rcFenceId;
    /**
     * uwb围栏名称
     */
	private String rcFenceName;
    /**
     * 标签类型
     */
	private Integer rcType;
    /**
     * 创建时间
     */
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;
    /**
     * 修改时间
     */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
}
