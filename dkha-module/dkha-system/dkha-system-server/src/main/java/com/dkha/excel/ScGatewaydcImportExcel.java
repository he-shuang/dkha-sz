package com.dkha.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 成都电科慧安
 * 网关302设备信息导入Excel
 *
 * @program: school
 * @description: ScGatewaydcImportExcel
 * @author: jinbiao
 * @create: 2020-08-31 09:21
 **/
@Data
public class ScGatewaydcImportExcel {


    /**
     * 网关设备编号
     */
    @ExcelProperty(value = "网关设备编号", index = 1)
    @ApiModelProperty(value = "网关设备编号")
    @NumberFormat("###")
    private String gwSn;

    /**
     * 网关设备名称
     */
    @ExcelProperty(value = "网关设备名称", index = 2)
    @ApiModelProperty(value = "网关设备名称")
    private String gwName;

    /**
     * 有效期
     */
    @ExcelProperty(value = "有效期", index = 4)
    @ApiModelProperty(value = "有效期")
    private String gwExpirydate;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期")
    private Date gwdate;

    /**
     * 网关IP
     */
    @ExcelProperty(value = "网关IP", index = 3)
    @ApiModelProperty(value = "网关IP")
    private String gwIpgateway;

    /**
     * 楼栋号
     */
    @ExcelProperty(value = "楼栋号", index = 5)
    private String dfName;

    /**
     * 楼层号
     */
    @ExcelProperty(value = "楼层号", index = 6)
    private String dfFloorname;

    /**
     * 总线
     */
    @ExcelProperty(value = "总线", index = 8)
    @ApiModelProperty(value = "485总线编号：1、2、3")
    private String gbdLineNum;

    /**
     * 房间号
     */
    @ExcelProperty(value = "房间号", index = 7)
    private String chNum;


    /**
     * PIR设备名称
     */
    @ExcelProperty(value = "PIR设备名称", index = 9)
    @ApiModelProperty(value = "PIR设备名称")
    private String PIRDevicesnName;

    /**
     * PIR设备编号
     */
    @ExcelProperty(value = "PIR设备编号", index = 10)
    @ApiModelProperty(value = "PIR设备编号")
    private String PIRDevicesn;

    /**
     * PIR485通讯地址编码
     */
    @ExcelProperty(value = "PIR485通讯地址编码", index = 11)
    @ApiModelProperty(value = "PIR485通讯地址编码")
    @NumberFormat("###")
    private Integer pirGbdAddr;

    /**
     * 智能照明设备编号
     */
    @ExcelProperty(value = "智能照明设备编号", index = 13)
    @ApiModelProperty(value = "智能照明设备编号")
    private String slDevicesn;

    /**
     * 智能照明设备名称
     */
    @ExcelProperty(value = "智能照明设备名称", index = 12)
    @ApiModelProperty(value = "智能照明设备名称")
    private String slDevicesnName;

    /**
     * SL485通讯地址编码
     */
    @ExcelProperty(value = "SL485通讯地址编码", index = 14)
    @ApiModelProperty(value = "SL485通讯地址编码")
    @NumberFormat("###")
    private Integer slGbdAddr;
    /**
     * PM2.5设备名称
     */
    @ExcelProperty(value = "PM2.5设备名称", index = 15)
    @ApiModelProperty(value = "PM2.5设备名称")
    private String PMDevicesnName;

    /**
     * PM2.5设备编号
     */
    @ExcelProperty(value = "PM2.5设备编号", index = 16)
    @ApiModelProperty(value = "PM2.5设备编号")
    private String PMDevicesn;

    /**
     * PM485通讯地址编码
     */
    @ExcelProperty(value = "PM485通讯地址编码", index = 17)
    @ApiModelProperty(value = "PM485通讯地址编码")
    @NumberFormat("###")
    private Integer pmGbdAddr;

    /**
     * 分组
     */
    @ExcelProperty(value = "组号", index = 18)
    private String gbdGroup;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注", index = 19)
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * uwb区域编号
     */
    @ExcelProperty(value = "uwb区域编号", index = 20)
    @ApiModelProperty(value = "uwb区域编号")
    private String gwUwbnum;

    /**
     * 序号
     */
    @ExcelProperty(value = "序号", index = 0)
    @ApiModelProperty(value = "序号")
    private String insertType;

    /**
     * 网关设备ID
     */
    private Long gwId;

    /**
     * 设备状态：-1 离线 0 正常 1 停用
     */
    private Integer gwState;

    /**
     * 设备安装地址
     */
    private String gwSetupaddr;

    /**
     * 楼层ID
     */
    @TableId
    private Long dfFloorid;

    /**
     * 设备安装日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gwSetupdate;

    List<ScGatebusdeviceImportExcel> sblist;
}
