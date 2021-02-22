package com.dkha.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  环境传感器报警
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/8/28 0028 15:02
 */
@Data
public class EnvironmentAlarmDTO {
    @ApiModelProperty(value = "网关编号")
    private  String  gwid;
    @ApiModelProperty(value = "ip地址")
    private  String  ipaddr;
    @ApiModelProperty(value = "485总线ID")
    private  String  gbdid;
    @ApiModelProperty(value = "485通讯地址编码")
    private  String  gbdaddr;
    @ApiModelProperty(value = "设备类型 0 PIR设备 1 智能控灯设备 2 PM2.5")
    private  String  gbddevicetype;
    @ApiModelProperty(value = "通讯设备ID")
    private  String  mbdid;
    @ApiModelProperty(value = "通讯设备SN")
    private  String  mbddevicesn;
    @ApiModelProperty(value = "uwb对应位置")
    private  String  mbduwbaddr;
    @ApiModelProperty(value = "PM 2.5的值")
    private   int pm;
    @ApiModelProperty(value = "温度报警值")
    private float temperature ;
    @ApiModelProperty(value = "湿度报警值")
    private float humidity ;
    @ApiModelProperty(value = "设备数据地址")
    private int slavenum;
    @ApiModelProperty(value = "报警时间戳（毫秒）")
    private String timestamp;
    @ApiModelProperty(value = "是否包含报警")
    private boolean needAlarm;


}
