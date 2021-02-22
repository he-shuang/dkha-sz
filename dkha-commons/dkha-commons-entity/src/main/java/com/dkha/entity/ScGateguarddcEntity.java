

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_gateguarddc")
public class ScGateguarddcEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 门禁设备ID
     */
	@TableId
	private Long ggdId;

    /**
     * 门禁设备编号
     */
	private String ggdSn;

    /**
     * 设备名称
     */
	private String ggdName;

    /**
     * 设备型号
     */
	private String ggdType;

    /**
     * 设备状态：-1 离线 0 正常 1停用
     */
	private Integer ggdStatus;

    /**
     * 设备安装日期
     */
	private Date ggdSetupdate;

    /**
     * 设备有效期到期日期
     */
	private Date ggdExpirydate;

    /**
     * 设备IP地址
     */
	private String ggdIpgateway;

    /**
     * 设备安装地址
     */
	private String ggdSetupaddr;

    /**
     * 楼层ID
     */
	private Long dfFloorid;

    /**
     * 房间ID
     */
	private Long drId;
}
