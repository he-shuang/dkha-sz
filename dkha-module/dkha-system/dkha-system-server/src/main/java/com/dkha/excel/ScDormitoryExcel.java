package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 房间及房间状态信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScDormitoryExcel {
    @Excel(name = "房间ID")
    private Long drId;
    @Excel(name = "楼层ID")
    private Long dfFloorid;
    @Excel(name = "房间编号")
    private String drNum;
    @Excel(name = "房间可入住人数")
    private Integer drCapacity;
    @Excel(name = "房间状态：0停用，1未入住，2已入住")
    private Integer drState;
    @Excel(name = "使用用途：0 学生宿舍  1 教师宿舍 2 教学楼")
    private Integer dfPurpose;

}
