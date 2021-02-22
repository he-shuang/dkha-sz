package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 电流互感器采集记录：每5分钟记录一次，并结合报警记录进行展示曲线给前端页面
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScCurrenthistoryExcel {
    @Excel(name = "电流互感器采集ID")
    private Long chId;
    @Excel(name = "电流互感器采集设备编号")
    private String chDevicesn;
    @Excel(name = "采集设备ID")
    private Long chDeviceid;
    @Excel(name = "采集电流值")
    private Float chEcurrent;
    @Excel(name = "采集时间")
    private Date chColltime;
    @Excel(name = "房间编号")
    private String chNum;
    @Excel(name = "房间ID")
    private Long chRoomid;

}
