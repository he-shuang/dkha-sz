package com.dkha.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 刷脸或卡记录表
 *
 * @author linhc linhc@dkay-cn.com
 * @since v1.0.0 2020-09-17
 */
@Data
public class ScFaceverificationExcel {

    @Excel(name = "照片" ,type = 2, width = 25, height = 75, imageType = 3)
    private byte[] imageUrlByte;

    @Excel(name = "设备名称")
    private String aeDevicename;

    @Excel(name = "识别人员名称")
    private String recognitionName;

    private Integer recordType;
    @Excel(name = "记录类型")
    private String recordTypeStr;

    private Integer verificationType;
    @Excel(name = "签到类型")
    private String verificationTypeStr;

    @Excel(name = "检测温度值")
    private BigDecimal temperature;

    @Excel(name = "打卡时间")
    private Date createDate;


    public String getVerificationTypeStr() {
        return this.verificationType == 1 ? "进" : "出";
    }

    public void setVerificationTypeStr(String verificationTypeStr) {
        this.verificationTypeStr = verificationTypeStr;
    }

    public String getRecordTypeStr() {
        return this.recordType == 1 ? "刷脸记录" : "刷IC卡记录";
    }

    public void setRecordTypeStr(String recordTypeStr) {
        this.recordTypeStr = recordTypeStr;
    }
}
