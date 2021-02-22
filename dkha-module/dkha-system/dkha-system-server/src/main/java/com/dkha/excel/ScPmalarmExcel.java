package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * PM2.5设备报警信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScPmalarmExcel {
    @Excel(name = "PM2.5报警ID")
    private Long pmaId;
    @Excel(name = "PM设备序列号")
    private String pmaDevicesn;
    @Excel(name = "PM设备ID")
    private Long pmaDeviceid;
    @Excel(name = "PM采集值")
    private Float pmaValue;
    @Excel(name = "PM报警时间")
    private Date pmaAlarmtime;
    @Excel(name = "报警对应位置")
    private String pmaAddress;

}
