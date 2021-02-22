package com.dkha.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 学生未归寝每日统计
 * </p>
 *
 * @author Spring
 * @since 2020-12-07
 */
@Data
@ApiModel(value="学生未归寝每日统计")
public class ScStudentsNotInSchoolDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "学生照片")
    private String scPhotoimg;

    @ApiModelProperty(value = "学生姓名")
    private String scStuname;

    @ApiModelProperty(value = "学生学号")
    private String scNo;

    @ApiModelProperty(value = "学生手机号")
    private String scPhonenum;

    @ApiModelProperty(value = "学院")
    private String scSchool;

    @ApiModelProperty(value = "房间编号")
    private String drNum;

    @ApiModelProperty(value = "出宿舍楼时间")
    private Date leaveDoorTime;

    @ApiModelProperty(value = "出教学楼时间")
    private Date leaveSchoolTime;

    @ApiModelProperty(value = "创建日期")
    private String date;

}
