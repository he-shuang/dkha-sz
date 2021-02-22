package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "5寸门禁设备对应下发人脸信息")
public class FvScDeviceFaceDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "5门禁的人员ID")
    private Integer fPersonId;

    @ApiModelProperty(value = "5门禁的设备ID")
    private Integer fDeviceId;

    @ApiModelProperty(value = "下发人脸地址")
    private String fFaceUrl;

    @ApiModelProperty(value = "5门禁的人员名称")
    private String fName;

    @ApiModelProperty(value = "5门禁的人员下发时间戳")
    private Long facedowntime;

    @ApiModelProperty(value = "5门禁的人员下发时间")
    private Date facedownDate;
}
