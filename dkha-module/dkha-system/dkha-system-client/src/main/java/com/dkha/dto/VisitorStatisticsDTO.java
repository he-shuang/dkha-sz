package com.dkha.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class VisitorStatisticsDTO {

    private String dateStr;

    private String floorname;

    private String count;

    private String name;

}
