package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 485通讯总线下挂载的设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScGatebusdeviceExcel {
    @Excel(name = "网关设备ID")
    private Long gwId;
    @Excel(name = "通讯设备ID")
    private Long mbdId;
    @Excel(name = "485通讯总线设备ID")
    private Long gbdId;
    @Excel(name = "485通讯地址编码")
    private Integer gbdAddr;
    @Excel(name = "设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5 ")
    private Integer gbdDevicetype;
    @Excel(name = "设备编号")
    private String gbdDevicesn;


}
