package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 网关302设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScGatewaydcExcel {
    @Excel(name = "网关设备ID")
    private Long gwId;
    @Excel(name = "网关设备编号")
    private String gwSn;
    @Excel(name = "网关设备名称")
    private String gwName;
    @Excel(name = "设备状态：-1 离线 0 正常 1 停用")
    private Integer gwState;
    @Excel(name = "设备安装日期")
    private Date gwSetupdate;
    @Excel(name = "设备有效期到期时间")
    private Date gwExpirydate;
    @Excel(name = "设备IP地址")
    private String gwIpgateway;
    @Excel(name = "设备安装地址")
    private String gwSetupaddr;
    @Excel(name = "设备uwb系统对应位置")
    private String gwUwbaddr;
    @Excel(name = "uwb区域编号")
    private String gwUwbnum;
    @Excel(name = "楼层ID")
    private Long dfFloorid;

}
