package com.dkha.commons.tools.excel;


import lombok.Data;

import java.util.List;

/**
 * 定义表格的数据对象
 * @author JIANGYOUYAO
 * @email 935090232@qq.com
 * @date 2017年12月20日
 */
@Data
public class ExcelSheetVO {

    /**
     * sheet的名称
     */
    private String sheetName;


    /**
     * 表格标题
     */
    private String title;

    /**
     * 头部标题集合
     */
    private String[] headers;

    /**
     * 数据集合
     */
    private List<List<Object>> dataList;

}
