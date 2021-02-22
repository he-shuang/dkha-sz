

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 门禁同行记录
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_gateaccesscontrol")
public class ScGateaccesscontrolEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 门禁开门ID
     */
	@TableId
	private Long gacId;

    /**
     * 门禁设备编号
     */
	private String gacSn;

    /**
     * 门禁方向：0 进  1 出
     */
	private Integer gacIotype;

    /**
     * 开门时间
     */
	private Date gacOpentime;

    /**
     * 开门人员姓名
     */
	private String gacName;

    /**
     * 开门人员图像地址
     */
	private String gacPersonimg;

    /**
     * 设备地址
     */
	private String gacDeviceadd;

    /**
     * 通行人温度
     */
	private Float gacTemperature;
}
