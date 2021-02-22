package com.dkha.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataAnalysisDTO {

    @ApiModelProperty(value = "学生姓名")
    private String scStuname;

    @ApiModelProperty(value = "学生手机号")
    private String scPhonenum;

    @ApiModelProperty(value = "学生性别：1 男 0女")
    private Integer scSex;

    @ApiModelProperty(value = "1宿舍 2教学楼")
    private Integer type;

    @ApiModelProperty(value = "详情")
    private List<DataAnalysisInfoDTO> dataAnalysisInfoDTOList;
}
