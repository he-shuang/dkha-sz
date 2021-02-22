

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 互感器宿舍关联关系
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_transformerroom")
public class ScTransformerroomEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 互感器关联房间ID
     */
	@TableId
	private Long tfrId;

    /**
     * 房间ID
     */
	private Long drId;

    /**
     * 互感器设备ID
     */
	private Long tfId;

    /**
     * 设备编号
     */
	private String tfDevicesn;

    /**
     * 宿舍房间编号
     */
	private String tfrDrroomno;

    /**
     * 互感器端口地址：0-3
     */
	private Integer tfrPortaddr;

    /**
     * 互感器关联时间
     */
	private Date tfrRelationdate;
}
