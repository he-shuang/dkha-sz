package com.dkha.dto.doorcontrol;

import lombok.Data;

@Data
public class SetDeviceLogoInfo {
    //	主Logo图片Base64编码	String	N	如果编码值字符串中包含”data:image/jpeg;base64,”内容，请先把该内容去除掉再请求；“operation”值为1时，此项必传
  private String  mainLogoBase64;
    //	副Logo图片Base64编码	String	N	“operation”值为3时，此项必传
    private String viceLogoBase64;
    //	操作类型	Int	Y	1：设置主Logo；2：删除主Logo；3：设置副Logo；4：删除副Logo
   private int  operation;
    //	管理端主Logo的Id，可以为随机码	String	N	“operation”值为1时，此项必传；若更改logo图片，此值必须更改
    private String  mainLogoId;
    //	管理端副Logo的Id，可以为随机码	String	N	“operation”值为3时，此项必传；若更改logo图片，此值必须更改
    private String  secondLogoId;
}
