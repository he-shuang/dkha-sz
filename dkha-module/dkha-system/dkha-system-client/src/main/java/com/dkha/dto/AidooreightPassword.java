package com.dkha.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AidooreightPassword {

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "设备IDS")
    private Long[] ids;
}
