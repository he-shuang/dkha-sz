package com.dkha.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 互感器采集数据
 *
 * @author Administrator
 * @version 1.0
 * @date 2020/8/31 0031 16:56
 */
@Data
public class RoadEctricCurrent {

    @ApiModelProperty(value = "是否包含报警")
    private boolean needAlarm;

    @ApiModelProperty(value = "电流值")
    private Float electricValue;

    @ApiModelProperty(value = "路数")
    private int road;

    @ApiModelProperty(value = "ip")
    private String ip;
    @ApiModelProperty(value = "采集时间")
    private Date collecttime;
}
