

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生档案信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_students")
public class ScStudentsEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 学生档案ID
     */
	@TableId
	private Long scStdid;

    /**
     * 学生姓名
     */
	private String scStuname;

    /**
     * 学生性别：0 男 1女
     */
	private Integer scSex;

    /**
     * 学生学号
     */
	private String scNo;

    /**
     * 学生身份证号
     */
	private String scIdno;

    /**
     * 学生入学时间
     */
	private String scRegisterdate;

    /**
     * 学生状态：0毕业，1在读,2肄业3,结业"
     */
	private Integer scStatus;

    /**
     * 学生学历：0本科，1硕士，2博士，3博士后
     */
	private Integer scEducation;

    /**
     * 学生照片地址
     */
	private String scPhotoimg;

    /**
     * 学生RFCardid编号
     */
	private String scRfcardid;

    /**
     * 学生uwb编号
     */
	private String scUwbid;

    /**
     * 学生手机号
     */
	private String scPhonenum;

    /**
     * 毕业时间
     */
	private String scGraduationdate;

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	private Long  creator;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private Date createDate;
	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Long updater;
	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateDate;
	/**
	 * 学生头像照片地址（主要是针对5寸门禁照片）
	 */
	private  String scHeadphotoimg;
	/**
	 * 所属学院
	 */
	private  String scSchool;
}
