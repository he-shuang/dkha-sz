package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 8英寸智能门禁设备具体的人脸信息
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-16
 */
@Data
public class ScAidooreightPersonlistExcel {
    @Excel(name = "主键ID")
    private Long apId;
    @Excel(name = "设备端ID")
    private Long aeId;
    @Excel(name = "设备SN号码")
    private String aeSerialnumber;
    @Excel(name = "人员姓名")
    private String username;
    @Excel(name = "用户ID")
    private Long userid;
    @Excel(name = "用户编号")
    private String userno;
    @Excel(name = "学生性别：1男 0女")
    private Integer sex;
    @Excel(name = "照片地址")
    private String photoimg;
    @Excel(name = "0 教师 1 保洁 2 保安   3 学生")
    private Integer persontype;
    @Excel(name = "")
    private Date updateDate;

}
