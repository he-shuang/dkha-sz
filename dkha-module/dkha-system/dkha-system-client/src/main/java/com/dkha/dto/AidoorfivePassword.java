package com.dkha.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AidoorfivePassword {
    @ApiModelProperty(value = "mqtt序列号")
    private String fSerial;

    @ApiModelProperty(value = "mqtt设备密码")
    private String fPassword;

    @ApiModelProperty(value = "设备名称")
    private String fName;
}
