package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * uwb报警内容：工具标签报警，访客禁区报警，保密区域报警
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScUwbalarmExcel {
    @Excel(name = "uwb报警ID")
    private Long ubaId;
    @Excel(name = "报警类型：0 工具标签报警  1 访客禁区报警 2 保密区域报警 ")
    private Integer ubaAlarmtype;
    @Excel(name = "uwb报警标签编号")
    private String ubaUwbid;
    @Excel(name = "uwb报警区域编号")
    private String ubaUwbregionno;
    @Excel(name = "报警时间")
    private Date ubaAlarmtime;
    @Excel(name = "报警是否已处理")
    private Integer ubaIshandler;
    @Excel(name = "报警对应的实际位置")
    private String ubaAddress;

}
