package com.dkha.excel;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

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
public class ScGatebusdeviceImportExcel {

    /**
     * 网关设备ID
     */
    @TableId
    private String gwId;

    /**
     * 通讯设备ID
     */
    private String mbdId;

    /**
     * 485通讯总线设备ID
     */
    private String gbdId;

    /**
     * 485通讯地址编码
     */
    private Integer gbdAddr;

    /**
     * 设备类型：0 PIR设备 1 智能控灯设备 2 PM2.5
     */
    private String gbdDevicetype;

    /**
     * 设备编号
     */
    private String gbdDevicesn;

    /**
     * 485总线编号：1、2、3
     */
    private String gbdLineNum;

    /**
     * 分组
     */
    private String gbdGroup;

    /**
     * uwb围栏ID
     */
    private Long gbdFenceId;

    /**
     * 围栏名称
     */
    private String fenceName;
}
