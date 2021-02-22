package com.dkha.model.aidoor.response;

import java.util.List;

public class Person {
    private long addTime;
    private long updateTime;
    private int personInfoType;
    private String icCardNo;
    private String personIdentifier;
    private String personName;
    private String personSerial;
    private List<PersonFace> faceList;

    public List<PersonFace> getFaceList() {
        return faceList;
    }

    public void setFaceList(List<PersonFace> faceList) {
        this.faceList = faceList;
    }

    public Person(List<PersonFace> faceList) {
        this.faceList = faceList;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getPersonInfoType() {
        return personInfoType;
    }

    public void setPersonInfoType(int personInfoType) {
        this.personInfoType = personInfoType;
    }

    public String getIcCardNo() {
        return icCardNo;
    }

    public void setIcCardNo(String icCardNo) {
        this.icCardNo = icCardNo;
    }

    public String getPersonIdentifier() {
        return personIdentifier;
    }

    public void setPersonIdentifier(String personIdentifier) {
        this.personIdentifier = personIdentifier;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonSerial() {
        return personSerial;
    }

    public void setPersonSerial(String personSerial) {
        this.personSerial = personSerial;
    }
}
