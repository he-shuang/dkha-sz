package com.dkha.dto.doorcontrol;

import lombok.Data;

@Data
public class DeviceLogoInfo {
    /**
     *  主Logo图片的Base64编码	String	当设备端与管理端的mainLogoId相同时，设备端返回空字符串
     */
  private String    mainLogo;
    /**
     *  主Logo的Id	String	当返回空字符串时，表示设备端主Logo未设置；
     *  否则，校验设备端与管理端的mainLogoId是否相同，不同时请更新管理端的主Logo,并更新数据库中的mainLogoId
     */
  private String   mainLogoId;
    /**
     * 副Logo图片的Base64编码	String	当设备端与管理端的secondLogoId相同时，设备端返回空字符串
     */
  private String   secondLogo;
    /**
     * 副Logo的Id	String	当返回空字符串时，表示设备端副Logo未设置；
     * 否则，校验设备端与管理端的secondLogoId是否相同，不同时请更新管理端的副Logo,并更新数据库中的secondLogoId
     */
  private String   secondLogoId;

}
