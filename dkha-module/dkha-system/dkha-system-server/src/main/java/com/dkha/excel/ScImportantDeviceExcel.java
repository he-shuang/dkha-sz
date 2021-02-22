package com.dkha.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 重要设备信息表
 */
@Data
public class ScImportantDeviceExcel {
    /**
     * 设备序列号
     */
    @ExcelProperty(value = "设备序列号", index = 1)
	private String imDevicesn;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称", index = 0)
    private String imDevicename;


    /**
     * 有效期
     */
    @ExcelProperty(value = "有效期", index = 2)
	private String imExpirydate;


    /**
     * 安装地址
     */
    @ExcelProperty(value = "安装位置", index = 3)
	private String imSetupaddr;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 5)
	private String remark;

    /**
     * uwb编号
     */
    @ExcelProperty(value = "uwb编号", index = 4)
	private String uwb;


}