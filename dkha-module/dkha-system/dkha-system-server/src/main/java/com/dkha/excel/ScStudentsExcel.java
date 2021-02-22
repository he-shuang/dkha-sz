package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 学生档案信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScStudentsExcel {
//    @Excel(name = "学生档案ID")
//    private Long scStdid;
    @Excel(name = "学生照片", type = 2, width = 25, height = 75, imageType = 3)
    private byte[] scPhotoimgByte;
    @Excel(name = "学生学号", width = 25)
    private String scNo;
    @Excel(name = "学生姓名", width = 25)
    private String scStuname;
    @Excel(name = "学生性别", width = 25, replace = {"男_1","女_0"})
    private Integer scSex;
    @Excel(name = "学生手机号", width = 25)
    private String scPhonenum;
    @Excel(name = "学生学历", width = 25, replace = {"本科_0","硕士_1","博士_2","博士后_3"})
    private Integer scEducation;
//    @Excel(name = "学生身份证号")
//    private String scIdno;
//    @Excel(name = "学生入学时间")
//    private Date scRegisterdate;
//    @Excel(name = "学生状态：0正常，1已毕业")
//    private Integer scStatus;
//    @Excel(name = "学生RFCardid编号")
//    private String scRfcardid;
//    @Excel(name = "学生uwb编号")
//    private String scUwbid;
//    @Excel(name = "毕业时间")
//    private Date scGraduationdate;

}
