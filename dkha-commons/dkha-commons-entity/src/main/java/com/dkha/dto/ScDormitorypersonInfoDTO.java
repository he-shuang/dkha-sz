package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 宿舍当前入住人员信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "宿舍当前入住人员信息")
public class ScDormitorypersonInfoDTO extends ScDormitoryDTO {
    private static final long serialVersionUID = 1L;


    List<ScDormitorypersonDTO> dormitorypersonDTOS;

}
