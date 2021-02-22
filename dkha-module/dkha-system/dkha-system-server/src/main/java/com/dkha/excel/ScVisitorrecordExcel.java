package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 访客记录表
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScVisitorrecordExcel {
    @Excel(name = "序号")
    private int num;
    @Excel(name = "照片", type = 2, width = 15, height = 35, imageType = 3)
    private byte[] scPhotoimgByte;
    @Excel(name = "访客姓名")
    private String vrName;
    private Integer vrSex;
    @Excel(name = "性别")
    private String sex;
    @Excel(name = "体温")
    private BigDecimal vrTemperature;
    @Excel(name = "访客对象")
    private String vrInterviewed;
    @Excel(name = "访客地址")
    private String vrAddress;
    @Excel(name = "访客开始时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date vrVistorbegintime;
    @Excel(name = "访客结束时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date vrVistorendtime;
    public String getSex() {
        return getVrSex() == 0 ? "女": "男";
    }

}
