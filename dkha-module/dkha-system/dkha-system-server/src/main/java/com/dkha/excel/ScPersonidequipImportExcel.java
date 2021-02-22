package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 人证识别设备
 *
 * @author Mark
 * @since v1.0.0 2020-08-26
 */
@Data
public class ScPersonidequipImportExcel {

    /**
     * 设备序列号
     */
    @ExcelProperty(value = "设备序列号", index = 2)
    private String pieEquipsn;


    /**
     * IP地址
     */
    @ExcelProperty(value = "IP地址", index = 1)
    private String pieIpaddr;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称", index = 0)
    private String pieDevicename;

    /**
     * 安装位置
     */
    @ExcelProperty(value = "安装位置", index = 4)
    private String pieSetupaddr;

    /**
     * 有效期
     */
    @ExcelProperty(value = "有效期", index = 3)
    private String pieExpirydate;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 5)
    private String remark;

}
