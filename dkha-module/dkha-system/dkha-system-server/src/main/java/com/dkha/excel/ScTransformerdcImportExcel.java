package com.dkha.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 互感器设备信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScTransformerdcImportExcel {

    /**
     * 设备编号
     */
    @ExcelProperty(value = "设备编号", index = 2)
    private String tfDevicesn;

    /**
     * 设备名称
     */
    @ExcelProperty(value = "设备名称", index = 1)
    private String tfDevicename;

    /**
     * 电流互感器IP
     */
    @ExcelProperty(value = "电流互感器IP", index = 4)
    private String tfIpgateway;

    /**
     * 检测端编号
     */
    @ExcelProperty(value = "检测端编号", index = 5)
    @NumberFormat("###")
    private String tfrPortaddr;
    /**
     * 楼栋号
     */
    @ExcelProperty(value = "楼栋号", index = 6)
    private String dfName;

    /**
     * 楼层号
     */
    @ExcelProperty(value = "楼层号", index = 7)
    private String dfFloorname;

    /**
     * 房间号
     */

    @ExcelProperty(value = "房间号", index = 8)
    @NumberFormat("###")
    private String chNum;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 9)
    private String remark;

    /**
     * 有效期
     */
    @ExcelProperty(value = "有效期", index = 3)
    private String tfExpirydate;

    /**
     * 有效期
     */
    private Date scDate;
    /**
     * 序号
     */
    @ExcelProperty(value = "用来分开存，序号", index = 0)
    private String tfDevicetype;

    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
    private Integer tfStatus;

    /**
     * 设备安装日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date tfSetupdate;

    /**
     * 设备安装地址
     */
    private String tfSetupaddr;

    /**
     * 楼层ID
     */
    private Long dfFloorid;

    /**
     * 互感器设备ID
     */
    private Long tfId;

    private List<ScTransformerroomExcel> listTfroom;


}
