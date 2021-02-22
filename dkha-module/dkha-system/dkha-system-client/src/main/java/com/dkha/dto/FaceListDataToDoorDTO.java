package com.dkha.dto;

import lombok.Data;

import java.util.List;

@Data
public class FaceListDataToDoorDTO {
    private  int usertype; //1 学生 2 教师 3 访客
    private List<String> userids;
    private List<String> aeids;
}
