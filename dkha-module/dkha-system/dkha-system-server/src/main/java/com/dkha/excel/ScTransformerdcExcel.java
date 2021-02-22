package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 互感器设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScTransformerdcExcel {
    @Excel(name = "互感器设备ID")
    private Long tfId;
    @Excel(name = "互感器设备编号")
    private String tfDevicesn;
    @Excel(name = "互感器设备名称")
    private String tfDevicename;
    @Excel(name = "设备类型")
    private String tfDevicetype;
    @Excel(name = "设备状态：-1 离线 0 正常 1 停用")
    private Integer tfStatus;
    @Excel(name = "设备安装日期")
    private Date tfSetupdate;
    @Excel(name = "设备有效期到期时间")
    private Date tfExpirydate;
    @Excel(name = "设备IP地址")
    private String tfIpgateway;
    @Excel(name = "设备安装地址")
    private String tfSetupaddr;
    @Excel(name = "楼层ID")
    private Long dfFloorid;

}
