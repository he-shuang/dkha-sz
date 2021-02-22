package com.dkha.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * uwb区域
 * @author xiedong
 * @version v1.0
 * @date 2020-09-01 13:42
 */
@Data
public class UwbRegionDTO {

    @ApiModelProperty(value = "围栏ID")
    private Long id;

    @ApiModelProperty(value = "围栏名称")
    private String fenceName;

    @ApiModelProperty(value = "地图ID")
    private Long mapId;

    @ApiModelProperty(value = "楼层")
    private String floor;

    @ApiModelProperty(value = "围栏坐标")
    private String coordinate;

    @ApiModelProperty(value = "")
    private Long adminId;

    @ApiModelProperty(value = "修改时间")
    private String updateTime;

    @ApiModelProperty(value = "创建时间")
    private String createTime;

    @ApiModelProperty(value = "修改者")
    private Long createUid;

    @ApiModelProperty(value = "创建者")
    private Long updateUid;
}
