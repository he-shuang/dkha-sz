package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 电流互感器房间电流信息报警
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScTransformalarmExcel {
    @Excel(name = "电流互感器报警ID")
    private Long tfaId;
    @Excel(name = "互感器设备编号")
    private String tfDevicesn;
    @Excel(name = "互感器设备ID")
    private Long tfId;
    @Excel(name = "报警电流值")
    private Float tfaEcurrent;
    @Excel(name = "报警时间")
    private Date tfaAlarmtime;
    @Excel(name = "房间编号")
    private String drNum;
    @Excel(name = "房间ID")
    private Long drRoomid;

}
