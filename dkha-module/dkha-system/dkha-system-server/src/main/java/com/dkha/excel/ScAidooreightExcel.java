package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 智能设备主要分为：8英寸智能门禁设备
 *
 * @author Mark sunlightcs@gmail.com
 * @since v1.0.0 2020-09-14
 */
@Data
public class ScAidooreightExcel {
    @Excel(name = "主键ID")
    private Integer aeId;
    @Excel(name = "设备端IP")
    private String aeClientip;
    @Excel(name = "设备端端口")
    private Integer aeClientport;
    @Excel(name = "设备Mac地址")
    private String aeMacaddress;
    @Excel(name = "设备SN号")
    private String aeSerialnumber;
    @Excel(name = "设备名称")
    private String aeDevicename;
    @Excel(name = "设备签到类型：1签到，2 签退，3 签到和签退")
    private Integer aeSigntype;
    @Excel(name = "签名密钥: 用于签名sign ,初步设计为使用   （MAC地址+ DKHA字符 ）  进行MD5摘要后的字符串来作为签名 （界面不显示）")
    private String aeSignkey;
    @Excel(name = "管理端分配给设备端的唯一标识，设备端在回调管理端接口时会返回此信息，可作为判断设备端的依据")
    private String aeDeviceid;
    @Excel(name = "")
    private Date aeSetupdate;
    @Excel(name = "设备有效期到期时间")
    private Date aeExpirydate;
    @Excel(name = "")
    private String aeSetupaddr;
    @Excel(name = "设备状态：-1 离线 0 正常 1 停用")
    private Integer aeState;
    @Excel(name = "")
    private String remark;
    @Excel(name = "楼层ID")
    private Integer dfFloorid;

}
