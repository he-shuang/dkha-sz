package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生未归寝每日统计
 * </p>
 *
 * @author Spring
 * @since 2020-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="ScStudentsNotInSchool对象", description="学生未归寝每日统计")
@TableName("sc_students_not_in_school")
public class ScStudentsNotInSchoolEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    /**
     *学生姓名
     */
    private String scStuname;

    /**
     * 学生照片
     */
    private String scPhotoimg;

    /**
     *学生学号
     */
    private String scNo;

    /**
     *学生手机号
     */
    private String scPhonenum;

    /**
     *学院
     */
    private String scSchool;

    /**
     *房间编号
     */
    private String drNum;

    /**
     *出宿舍楼时间
     */
    private Date leaveDoorTime;

    /**
     *出教学楼时间
     */
    private Date leaveSchoolTime;

    /**
     * 创建日期
     */
    private String date;

}
