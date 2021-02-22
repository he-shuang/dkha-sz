package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 人证识别设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Data
public class ScPersonidequipExcel {
    @Excel(name = "设备ID")
    private Long pieId;
    @Excel(name = "设备编号")
    private String pieEquipsn;
    @Excel(name = "人证设备IP")
    private String pieIpaddr;
    @Excel(name = "设备安装地址")
    private String pieSetupaddr;
    @Excel(name = "通讯设备名称")
    private String pieDevicename;
    @Excel(name = "设备状态：-1 离线 0 正常 1 停用")
    private Integer pieStatus;
    @Excel(name = "设备安装日期")
    private Date pieSetupdate;
    @Excel(name = "设备有效期到期时间")
    private Date pieExpirydate;
    @Excel(name = "楼层ID")
    private Long dfFloorid;
    @Excel(name = "是否初始化配置：0 未初始化，1已初始化")
    private Integer pieIsinitial;

}
