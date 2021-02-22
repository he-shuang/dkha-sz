package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 宿舍当前入住人员信息
 *
 * @since v1.0.0 2020-08-23
 */
@ExcelTarget("20")
@Data
public class ScDormitorypersonExcel {

    @Excel(name ="学院",width = 45)
    private String scSchool;
    @Excel(name ="楼层")
    private String dfFloorname;
    @Excel(name ="宿舍号")
    private String drNum;

    @Excel(name ="学生姓名")
    private String scStuname;

    @Excel(name ="联系电话",width = 35)
    private String scPhonenum;

    @Excel(name ="未归寝时间",width = 45)
    private String updateDate;

    @Excel(name ="是否在教学楼",width = 45)
    private String isTeachingBuilding;

    @Excel(name ="打卡时间",width = 45,format = "yyyy-MM-dd HH:mm:ss")
    private Date checkDate;

    private Long scStdid;


}
