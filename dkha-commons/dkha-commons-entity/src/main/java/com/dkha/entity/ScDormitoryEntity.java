

package com.dkha.entity;

import com.baomidou.mybatisplus.annotation.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 房间及房间状态信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("sc_dormitory")
public class ScDormitoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;


    /**
     * 房间ID
     */
	@TableId
	private Long drId;

    /**
     * 楼层ID
     */
	private Long dfFloorid;

    /**
     * 房间编号
     */
	private String drNum;

    /**
     * 房间可入住人数
     */
	private Integer drCapacity;

    /**
     * 房间状态：0停用，1未入住，2已入住
     */
	private Integer drState;

    /**
     * 使用用途：0 学生宿舍  1 教师宿舍 2 教学楼
     */
	private Integer dfPurpose;

    /**
     * 是否已住满：0 未住满，1 已住满
     */
	private Integer dfIsfull;
    /**
     * 是否正在上传人脸：0 未上传，1 正在上传
     */
	private Integer isUpload;
    /**
     * 当前下发人数
     */
	private String uploadNumber;
    /**
     * 已经下发成功人数
     */
	private String uploadSuccessNumber;

	/**
	 * 上级名称
	 */
	@TableField(exist = false)
	private String parentName;

	@ApiModelProperty(value = "报警状态： 0 报警 1 未报警")
	private Integer isAlarm;
}
