

package com.dkha.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 485通讯设备：PIR设备，PM2.5设备 ，智能控灯设备 导入信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScModbusdevicedcExcel implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 通讯设备ID
     */
	@TableId
	private Long mbdId;

    /**
     * 通讯设备编号
     */
    @ExcelProperty(value  = "设备序列号(*)",index = 1)
	private String mbdDevicesn;

    /**
     * 通讯设备名称
     */
    @ExcelProperty(value  = "设备名称(*)",index = 0)
	private String mbdDevicename;

    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
	private Integer mdbStatus;

    /**
     * 设备安装日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date mbdSetupdate;

    /**
     * 设备有效期到期时间
     */
//	@JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    @ExcelProperty(value  = "有效期(*)",index = 2)
	private String mbdExpirydate;

    /**
     * 设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5
     */
	private Integer mbdDevicetype;

    /**
     * uwb对应位置
     */
    @ExcelProperty(value  = "安装位置(楼栋-楼层-房间号)(*)",index = 3)
	private String mbdUwbaddr;

    /**
     * 楼层ID
     */
	private Long dfFloorid;

	/**
	 * 是否组网:0否；1是
	 */
	private Integer mbdNetwork;

	/**
	 * 备注
	 */
    @ExcelProperty(value  = "备注",index = 4)
	private String remark;

    @ApiModelProperty(value = "错误提示")
    @ExcelProperty(value  = "错误提示")
    private String message;
}
