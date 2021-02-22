package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 8英寸智能门禁设备每日采集数量
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@Data
public class ScAidooreightDailyExcel {
    @Excel(name = "设备ID")
    private Long aeId;
    @Excel(name = "采集时间")
    private Date mdGatherdate;
    @Excel(name = "采集数量")
    private Long mdGathercount;
    @Excel(name = "更新时间")
    private Date updateDate;

}