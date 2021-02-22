package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 楼栋，楼层信息
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScDormitoryfloorExcel {
    @Excel(name = "楼层ID")
    private Long dfFloorid;
    @Excel(name = "楼层/楼栋名称")
    private String dfFloorname;
    @Excel(name = "上级楼层信息ID")
    private Long dfParentid;
    @Excel(name = "楼层类型：0 楼栋，1 楼层")
    private Integer dfType;
    @Excel(name = "排序序号：数字从小到大显示")
    private Integer dfOrder;
    @Excel(name = "创建人ID")
    private Long creator;
    @Excel(name = "创建时间")
    private Date createDate;
    @Excel(name = "修改人ID")
    private Long updater;
    @Excel(name = "修改时间")
    private Date updateDate;
    @Excel(name = "使用用途：0 学生宿舍  1 教师宿舍 2 教学楼")
    private Integer dfPurpose;

}
