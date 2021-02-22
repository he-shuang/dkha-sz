package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 互感器宿舍关联关系
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScTransformerroomExcel {
    @Excel(name = "互感器关联房间ID")
    private Long tfrId;
    @Excel(name = "房间ID")
    private Long drId;
    @Excel(name = "互感器设备ID")
    private Long tfId;
    @Excel(name = "设备编号")
    private String tfDevicesn;
    @Excel(name = "宿舍房间编号")
    private String tfrDrroomno;
    @Excel(name = "互感器端口地址：0-3")
    private Integer tfrPortaddr;
    @Excel(name = "互感器关联时间")
    private Date tfrRelationdate;

}
