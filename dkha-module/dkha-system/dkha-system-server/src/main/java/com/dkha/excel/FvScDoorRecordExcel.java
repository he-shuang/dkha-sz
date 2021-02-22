package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * 开门记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-25
 */
@Data
public class FvScDoorRecordExcel {
    @Excel(name = "")
    private Integer fId;
    @Excel(name = "添加时间")
    private Integer fAddTime;
    @Excel(name = "权限ID")
    private String fKeyId;
    @Excel(name = "设备ID")
    private Integer fDeviceId;
    @Excel(name = "开门类型,1- 卡号 2- 注册用户id   3-蓝牙发的卡号（手机号） 4-密码开门   5-指纹ID  6-人脸ID")
    private Integer fOpenType;
    @Excel(name = "通过时间")
    private Integer fOpenTime;
    @Excel(name = "合法标志, 0-非法 1-合法")
    private Integer fState;
    @Excel(name = "人员ID")
    private Integer fPersonId;
    @Excel(name = "邀请人")
    private String fInviterName;
    @Excel(name = "代理商")
    private Integer fAgentId;

}