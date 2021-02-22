package com.dkha.dto.doorcontrol;

import lombok.Data;
import java.util.List;

@Data
public class DeviceInnerPerson {

   private long addTime;       //	添加时间	Long	毫秒时间戳
   private long updateTime;    //	数据更新时间	Long	毫秒时间戳
   private String personName;  //	人员姓名	String
   private String   personIdentifier; //	人员ID	String	不唯一，随业务需求变动，可以代表学号、工号等
   private String   personSerial;	//人员唯一序列码	String	唯一，不能重复
   private List<PersonFaceBase64>  faceList;//	人员人脸照片	List
   private String icCardNo	;   //人员的IC卡编号	String	由数字、字母组成，不超过50个字符
   private int  personInfoType;	//人员信息类型	Int	对应“3.6”接口中的“personInfoType”

}
