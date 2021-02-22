package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 门禁同行记录
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScGateaccesscontrolExcel {
    @Excel(name = "门禁开门ID")
    private Long gacId;
    @Excel(name = "门禁设备编号")
    private String gacSn;
    @Excel(name = "门禁方向：0 进  1 出")
    private Integer gacIotype;
    @Excel(name = "开门时间")
    private Date gacOpentime;
    @Excel(name = "开门人员姓名")
    private String gacName;
    @Excel(name = "开门人员图像地址")
    private String gacPersonimg;
    @Excel(name = "设备地址")
    private String gacDeviceadd;

}
