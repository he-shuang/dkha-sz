package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 互感器设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScStudentsNotInSchoolExcel {
    private static final long serialVersionUID = 1L;

    @Excel(name = "学生姓名")
    private String scStuname;
    @Excel(name = "学生学号")
    private String scNo;
    @Excel(name = "学生手机号")
    private String scPhonenum;
    @Excel(name = "学院")
    private String scSchool;
    @Excel(name = "房间编号")
    private String drNum;
    @Excel(name = "出宿舍楼时间")
    private Date leaveDoorTime;
    @Excel(name = "出教学楼时间")
    private Date leaveSchoolTime;

}
