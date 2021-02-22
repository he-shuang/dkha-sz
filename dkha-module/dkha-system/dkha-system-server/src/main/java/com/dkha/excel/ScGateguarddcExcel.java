package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 *
 *
 * @since v1.0.0 2020-08-23
 */
@Data
public class ScGateguarddcExcel {
    @Excel(name = "门禁设备ID")
    private Long ggdId;
    @Excel(name = "门禁设备编号")
    private String ggdSn;
    @Excel(name = "设备名称")
    private String ggdName;
    @Excel(name = "设备型号")
    private String ggdType;
    @Excel(name = "设备状态：-1 离线 0 正常 1停用")
    private Integer ggdStatus;
    @Excel(name = "设备安装日期")
    private Date ggdSetupdate;
    @Excel(name = "设备有效期到期日期")
    private Date ggdExpirydate;
    @Excel(name = "设备IP地址")
    private String ggdIpgateway;
    @Excel(name = "设备安装地址")
    private String ggdSetupaddr;
    @Excel(name = "楼层ID")
    private Long dfFloorid;
    @Excel(name = "房间ID")
    private Long drId;

}
