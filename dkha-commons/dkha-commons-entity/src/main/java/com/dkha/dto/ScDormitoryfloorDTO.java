package com.dkha.dto;

import com.dkha.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


/**
 * 楼栋，楼层信息
 *
 * @author Mark
 * @since v1.0.0 2020-08-23
 */
@Data
@ApiModel(value = "楼栋，楼层信息")
public class ScDormitoryfloorDTO implements Serializable {
    private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "楼层ID")
	private Long dfFloorid;

	@ApiModelProperty(value = "楼层/楼栋名称")
	@Length(max = 10,message = "名称长度不能超过10个字符", groups = DefaultGroup.class)
	private String dfFloorname;

	@ApiModelProperty(value = "上级楼层信息ID")
	private Long dfParentid;

	@ApiModelProperty(value = "楼层类型：0 楼栋，1 楼层")
	private Integer dfType;

	@ApiModelProperty(value = "排序序号：数字从小到大显示")
	private Integer dfOrder;

	@ApiModelProperty(value = "创建人ID")
	private Long creator;

	@ApiModelProperty(value = "创建时间")
	private Date createDate;

	@ApiModelProperty(value = "修改人ID")
	private Long updater;

	@ApiModelProperty(value = "修改时间")
	private Date updateDate;

	@ApiModelProperty(value = "使用用途：0 学生宿舍  1 教师宿舍 2 教学楼")
	private Integer dfPurpose;

	@ApiModelProperty(value = "是否有子节点")
	private Boolean hasChildren;

	@ApiModelProperty(value = "上级区域名称")
	private String parentName;

	@ApiModelProperty(value = "房间可入住人数")
	private Integer drCapacity;

	@ApiModelProperty(value = "UWB楼栋/楼层")
	private String[] uwbFloors;

}
