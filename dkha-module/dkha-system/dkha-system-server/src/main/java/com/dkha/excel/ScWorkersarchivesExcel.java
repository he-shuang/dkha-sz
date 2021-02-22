package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 教职工档案
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScWorkersarchivesExcel {
//    @Excel(name = "职工档案ID")
//    private Long scWaid;
    @Excel(name = "职工照片", type = 2, width = 25, height = 75, imageType = 3)
    private byte[] scPhotoimgByte;
    @Excel(name = "职工姓名", width = 25)
    private String scWaname;
    @Excel(name = "职工编号", width = 25)
    private String scEmpno;
    @Excel(name = "性别",replace = {"男_1","女_0"}, width = 25)
    private Integer scWasex;
    @Excel(name = "职工入职时间", width = 25)
    private String scHiredate;
    @Excel(name = "职工类型",replace = {"教师_0","保洁_1","保安_2"}, width = 25)
    private Integer scEmptype;
//    @Excel(name = "状态： -1 离职  0 正常 ")
//    private Integer scStatus;
//    @Excel(name = "职工RFCardid编号")
//    private String scRfcardid;
//    @Excel(name = "职工uwb编号")
//    private String scUwbid;
//    @Excel(name = "职工手机号")
//    private String scPhonenum;
//    @Excel(name = "身份证号")
//    private String scIdno;

}
