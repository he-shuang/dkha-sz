package com.dkha.dto.doorcontrol;

import lombok.Data;

@Data
public class PersonDoorAuthorityTimes {
    private  String   personSerial;       //	人员编号	String	Y
    private  String    authorityName;     //	权限名称	String	N	暂时未使用
    private  String     morningStartTime; //	通行权限1开始时间	String	N	如：08:30:30，结束时间不能早于或等于开始时间
    private  String      morningEndTime;  //	通行权限1结束时间	String	N	如：10:00:00
    private  String      noonStartTime;   //	通行权限2开始时间	String	N	如：11:30:30，结束时间不能早于或等于开始时间
    private  String     noonEndTime;      //	通行权限2结束时间	String	N	如：13:30:30
    private  String    nightStartTime;    //	通行权限3开始时间	String	N	如：17:30:30，结束时间不能早于或等于开始时间
    private  String    nightEndTime;      //	通行权限3结束时间	String	N	如：21:30:30

}
