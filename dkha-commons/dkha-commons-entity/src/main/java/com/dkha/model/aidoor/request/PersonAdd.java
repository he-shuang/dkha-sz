package com.dkha.model.aidoor.request;

import java.util.List;

public class PersonAdd {

    /**
     * 即将添加到设备端人脸的总数
     */
    private int total;

    /**
     * 人员编号
     */
    private String personSerial;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 人员工号
     */
    private String personIdentifier;

    /**
     * 人员注册照列表
     */
    private List<PersonAddFace> faceList;

    /**
     * IC卡编号
     */
    private String icCardNo;

    /**
     * 人员信息类型：1 只含有IC卡号；2 只含有人脸信息；3 以上两者都有
     */
    private int personInfoType;

    public int getPersonInfoType() {
        return personInfoType;
    }

    public void setPersonInfoType(int personInfoType) {
        this.personInfoType = personInfoType;
    }

    public String getPersonSerial() {
        return personSerial;
    }

    public void setPersonSerial(String personSerial) {
        this.personSerial = personSerial;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public List<PersonAddFace> getFaceList() {
        return faceList;
    }

    public void setFaceList(List<PersonAddFace> faceList) {
        this.faceList = faceList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPersonIdentifier() {
        return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
        this.personIdentifier = personIdentifier;
    }

    public String getIcCardNo() {
        return icCardNo;
    }

    public void setIcCardNo(String icCardNo) {
        this.icCardNo = icCardNo;
    }
}
