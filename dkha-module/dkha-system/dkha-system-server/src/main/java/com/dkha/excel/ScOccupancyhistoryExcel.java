package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 某房间的入住历史记录
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScOccupancyhistoryExcel {
    @Excel(name = "入住历史ID")
    private Long ohId;
    @Excel(name = "房间ID")
    private Long drId;
    @Excel(name = "入住学生姓名")
    private String stuname;
    @Excel(name = "入住学生学号")
    private String stuno;
    @Excel(name = "入住学生档案ID")
    private Long stuid;
    @Excel(name = "入住时间")
    private Date checkinDate;
    @Excel(name = "退房时间")
    private Date checkoutDate;
    @Excel(name = "创建人ID")
    private Long creator;

}
