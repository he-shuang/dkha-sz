package com.dkha.commons.tools.excel;

import lombok.Data;

@Data
public class PoiModel {
    //内容
    private String content;
    //上一行同一位置内容
    private String oldContent;
    //行标
    private int rowIndex;
    //列标
    private int cellIndex;
}
