package com.dkha.dto.doorcontrol;

import lombok.Data;

import java.util.List;

@Data
public class AddPersonRequestInfo {
  private  int    total	;            // 一段时间内，添加到设备端人员的总数	Int	Y	设备端会判断该参数大小，total>100会弹框提示用户正在注册人员
  private String  personSerial;      //	人员编号	String	Y	唯一，不能重复
  private String   personName;       //	人员姓名	String	Y
  private String  personIdentifier;  //	人员工号/学号等	String	N	不唯一，随业务需求变动，可以代表学号、工号等
  private List<PersonFaceBase64> faceList; //	人员人脸照片	List	N	目前只取第一条数据
  private String  icCardNo;        //	IC卡编号	String	N	IC卡编号和人员人脸照片可以二选一传输或者两者都传输；数字、字母组成，长度不超过50个字符
  private int  personInfoType;       //	人员信息类型	Int	Y	1：只有IC卡编号； 2：只有人脸信息；3：以上两者都有
  private String imagepath;        // 图片路径
}
