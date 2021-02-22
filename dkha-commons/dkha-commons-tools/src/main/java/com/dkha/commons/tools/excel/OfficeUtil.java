package com.dkha.commons.tools.excel;

import com.alibaba.csp.sentinel.util.StringUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.*;

public class OfficeUtil {

    /**
     * @param dataList    数据
     * @param headNameMap 标题
     * @param type        类型 1 xls 2 xlsx
     * @param mergeIndex  需要合并的列 从1开始  0是序号
     * @param benchmarkColumn 基准列（就是以那一列为标准来决定合不合并）
     * @return
     * @throws Exception
     */
    public static byte[] toExcelMergeCell(List<?> dataList, Map<String, String> headNameMap, int type, int[] mergeIndex, List<Integer> benchmarkColumn) throws Exception {
        Workbook workbook;
        if (type == 1) {
            workbook = new XSSFWorkbook();
        } else if (type == 2) {
            workbook = new SXSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }
        List<Method> methodList = null;
        Sheet sheet = workbook.createSheet("数据列表");
        sheet.setDefaultColumnWidth(30);// 默认列宽
        CellStyle cellStyleHeader = workbook.createCellStyle();
        cellStyleHeader.setAlignment(HorizontalAlignment.CENTER);
        cellStyleHeader.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyleHeader.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyleHeader.setBorderTop(BorderStyle.THIN);//上边框
        cellStyleHeader.setBorderRight(BorderStyle.THIN);//右边框
        cellStyleHeader.setWrapText(true);//自动换行
        Font font = workbook.createFont();
        font.setFontName("黑体");
//        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        cellStyleHeader.setFont(font);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        cellStyle.setWrapText(true);//自动换行

//             sheet.setColumnWidth(2, 6250);
        int index = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < dataList.size(); i++) {
            Object object = dataList.get(i);
            if (methodList == null) {
                Method[] methods = object.getClass().getMethods();
                methodList = new ArrayList<>();
                Row rowHead = sheet.createRow(index);
                Cell cell1 = rowHead.createCell(0);
                cell1.setCellValue("序号");
                cell1.setCellStyle(cellStyleHeader);
                Iterator<Map.Entry<String, String>> iterator = headNameMap.entrySet().iterator();
                int c = 1;
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    for (int m = 0; m < methods.length; m++) {
                        if (methods[m].getName().toLowerCase().equals(("get" + entry.getKey()).toLowerCase())) {
                            methodList.add(methods[m]);
                            Cell cell = rowHead.createCell(c);
                            setCellValue(cell, entry.getValue());
                            cell.setCellStyle(cellStyleHeader);
                            c++;
                        }
                        if (methods[m].getName().toLowerCase().equals(("getlist"))) {
                            Object invoke = methods[m].invoke(object);
                        }
                    }
                }
            }
            Row row = sheet.createRow(index + 1);
            Cell cell1 = row.createCell(0);
            cell1.setCellValue(i + 1);
            cell1.setCellStyle(cellStyle);
            for (int m = 0; m < methodList.size(); m++) {
                Object value = methodList.get(m).invoke(object);
                Cell cell = row.createCell(m + 1);
                Object textValue = getValue(value);
                setCellValue(cell, textValue);
                cell.setCellStyle(cellStyle);
            }
            index++;
        }
        String str = null;
        int strBeginIndex;
        int strEndIndex;
        int j;
        int start;
        int end = 0;
        Map<Integer, Integer> benchmarkMap = new LinkedHashMap<>();
        for (int i = 0; i < mergeIndex.length; i++) {
            j = 0;
            start = 0;
            strBeginIndex = 0;
            strEndIndex = 0;
            if (mergeIndex[i] >= 11 && mergeIndex[i] <= 12) {
                for (Integer endIndex : benchmarkMap.keySet()) {
                    CellRangeAddress region = new CellRangeAddress(benchmarkMap.get(endIndex), endIndex, mergeIndex[i], mergeIndex[i]);
                    sheet.addMergedRegion(region);
                }
            }
            for (Row row : sheet) {
                if (j == 0) {
                    j++;
                    continue;
                }
                if (StringUtil.isEmpty(str)) {
                    if (null != row.getCell(mergeIndex[i])) {
                        str = loadCellType(row.getCell(mergeIndex[i]));
//                             str = row.getCell(mergeIndex[i]).getStringCellValue();
                    }
                    if (str.equals(loadCellType(sheet.getRow(2).getCell(1)))) {
//                         if (str.equals(sheet.getRow(2).getCell(1).getStringCellValue())) {
                        strBeginIndex = row.getRowNum();
                    }
                } else if (str.equals(loadCellType(row.getCell(mergeIndex[i])))) {
//                     } else if (str.equals(row.getCell(mergeIndex[i]).getStringCellValue())) {
                    if (strBeginIndex == 0) {
                        strBeginIndex = sheet.getRow(j - 1).getRowNum();
                    }
                    strEndIndex = sheet.getLastRowNum();
                    end = strEndIndex;
                    if (sheet.getLastRowNum() == j) {
                        //末尾合并
                        if (benchmarkColumn.contains(mergeIndex[i])) {
                            CellRangeAddress region = new CellRangeAddress(strBeginIndex, strEndIndex, mergeIndex[i], mergeIndex[i]);
                            sheet.addMergedRegion(region);
                        } else if (!benchmarkMap.isEmpty() && null != benchmarkColumn && benchmarkColumn.size() > 0) {
                            for (Integer integer : benchmarkColumn) {
                                if(mergeIndex[i] > integer){
                                    consolidatedColumn(benchmarkMap, strBeginIndex, strEndIndex, mergeIndex[i], end, sheet);
                                    break;
                                }
                            }

                        }
                        if (mergeIndex[i] == 1) {
                            benchmarkMap.put(strEndIndex, strBeginIndex);
                        }
                    }
                } else if (!str.equals(loadCellType(row.getCell(mergeIndex[i])))) {
                    strEndIndex = row.getRowNum();
                    if (start == 0 && strBeginIndex > 0 && strEndIndex > 0) {
                        //首行合并
                        strEndIndex = strEndIndex - 1;
                        end = strEndIndex;
                        if (benchmarkColumn.contains(mergeIndex[i])) {
                            CellRangeAddress region = new CellRangeAddress(strBeginIndex, strEndIndex, mergeIndex[i], mergeIndex[i]);
                            sheet.addMergedRegion(region);
                        } else if (!benchmarkMap.isEmpty() && null != benchmarkColumn && benchmarkColumn.size() > 0) {
                            for (Integer integer : benchmarkColumn) {
                                if(mergeIndex[i] > integer){
                                    consolidatedColumn(benchmarkMap, strBeginIndex, strEndIndex, mergeIndex[i], end, sheet);
                                    break;
                                }
                            }
                        }
                        if (mergeIndex[i] == 1) {
                            benchmarkMap.put(strEndIndex, strBeginIndex);
                        }
                        strBeginIndex = 0;
                        start = 1;
                    } else if (strBeginIndex > 0 && strEndIndex > 0) {
                        //中间行合并
                        strEndIndex = strEndIndex - 1;
                        end = strEndIndex;
                        if (benchmarkColumn.contains(mergeIndex[i])) {
                            CellRangeAddress region = new CellRangeAddress(strBeginIndex, strEndIndex, mergeIndex[i], mergeIndex[i]);
                            sheet.addMergedRegion(region);
                        } else if (!benchmarkMap.isEmpty() && null != benchmarkColumn && benchmarkColumn.size() > 0) {
                            for (Integer integer : benchmarkColumn) {
                                if(mergeIndex[i] > integer){
                                    consolidatedColumn(benchmarkMap, strBeginIndex, strEndIndex, mergeIndex[i], end, sheet);
                                    break;
                                }
                            }
                        }
                        if (mergeIndex[i] == 1) {
                            benchmarkMap.put(strEndIndex, strBeginIndex);
                        }
                        strBeginIndex = 0;

                    }
                    str = loadCellType(row.getCell(mergeIndex[i]));
                }
                j++;
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        return baos.toByteArray();
    }

    private static String loadCellType(Cell cell){
        String value = null;
        switch (cell.getCellType()){
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case NUMERIC:
                // 判断当前的cell是否为Date
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    value = formateDate(cell.getDateCellValue());
                } else if(isDouble(cell)){
                    value = String.valueOf((double) cell.getNumericCellValue());
                }else{
                    value = String.valueOf((long) cell.getNumericCellValue());
                }
                break;
            case STRING:
                value = cell.getStringCellValue();
                break;
            case FORMULA:
                break;
        }
        return value;
    }

    private static String formateDate(Date date){
        if(null == date){
            return null;
        }
        SimpleDateFormat fo = null;
        try{
            fo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return fo.format(date);
        }catch (Exception e){
            try{
                fo = new SimpleDateFormat("yyyy-MM-dd");
                return fo.format(date);
            }catch (Exception e1){
                fo = new SimpleDateFormat("yyyy-MM");
                return fo.format(date);
            }
        }
    }

    public static boolean isDouble(Cell cell){
        if (cell == null) return false;
        boolean bDate = false;
        CellStyle style = cell.getCellStyle();
        String check = ""+cell.getNumericCellValue();
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        if (pattern.matcher(check).matches()) {
            return true;
        }else {
            return false;
        }
    }
    private static void consolidatedColumn(Map<Integer,Integer> benchmarkMap,Integer strBeginIndex,Integer strEndIndex,Integer mergeIndex,Integer end,Sheet sheet){
        for (Integer endIndex : benchmarkMap.keySet()) {
            if (strBeginIndex>=benchmarkMap.get(endIndex)&&strEndIndex>=endIndex&&strBeginIndex<endIndex){
                strEndIndex=endIndex;
                if (strBeginIndex<strEndIndex){
                    CellRangeAddress region = new CellRangeAddress(strBeginIndex, strEndIndex,  mergeIndex,  mergeIndex);
                    sheet.addMergedRegion(region);
                }
                strBeginIndex=strEndIndex+1;
                strEndIndex=end;
            }else if (strBeginIndex>=benchmarkMap.get(endIndex)&&strEndIndex<=endIndex&&strBeginIndex<endIndex){
                if (strBeginIndex<strEndIndex){
                    CellRangeAddress region = new CellRangeAddress(strBeginIndex, strEndIndex,  mergeIndex,  mergeIndex);
                    sheet.addMergedRegion(region);
                }
                strBeginIndex=strEndIndex+1;
                strEndIndex=end;
            }else if (strBeginIndex<benchmarkMap.get(endIndex)&&strEndIndex>=endIndex&&strBeginIndex<endIndex){
                strBeginIndex=benchmarkMap.get(endIndex);
                strEndIndex=endIndex;
                if (strBeginIndex<strEndIndex){
                    CellRangeAddress region = new CellRangeAddress(strBeginIndex, strEndIndex,  mergeIndex,  mergeIndex);
                    sheet.addMergedRegion(region);
                }
                strBeginIndex=strEndIndex+1;
                strEndIndex=end;
            }else if (strBeginIndex<=benchmarkMap.get(endIndex)&&strEndIndex<=endIndex&&strBeginIndex<endIndex){
                if (!isSection(benchmarkMap,strBeginIndex)){
                    strBeginIndex=benchmarkMap.get(endIndex);
                    if (strBeginIndex<strEndIndex){
                        CellRangeAddress region = new CellRangeAddress(strBeginIndex, strEndIndex,  mergeIndex,  mergeIndex);
                        sheet.addMergedRegion(region);
                    }
                    strBeginIndex=strEndIndex+1;
                    strEndIndex=end;
                }
            }
        }
    }
    private static Object getValue(Object value) {
        Object textValue = "";
        if (value != null) {
            if (value instanceof Boolean) {
                textValue = (Boolean) value ? "是" : "否";
            } else if (value instanceof Date) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                textValue = simpleDateFormat.format((Date) value);
            } else if (value instanceof String) {
                String val = (String) value;
                textValue = StringUtil.isEmpty(val) || "null".equalsIgnoreCase(val) ? "" : val;
            } else {
                textValue = value;
            }
        }
        return textValue;
    }
    private static boolean isSection(Map<Integer,Integer> benchmarkMap,Integer value){
        for (Integer integer : benchmarkMap.keySet()) {
            if (value>=benchmarkMap.get(integer)&&value<=integer){
                return true;
            }
        }
        return false;
    }
    private static void setCellValue(Cell cell, Object value) {
        if (value != null) {
            if (value instanceof Integer) {
                cell.setCellValue((Integer) value);
            } else if (value instanceof Boolean) {
                Boolean booleanValue = (Boolean) value;
                cell.setCellValue(booleanValue);
            } else if (value instanceof Date) {
                Date dateValue = (Date) value;
                cell.setCellValue(dateValue);
            } else if (value instanceof Float) {
                Float floatValue = (Float) value;
                cell.setCellValue(floatValue);
            } else if (value instanceof Double) {
                Double doubleValue = (Double) value;
                cell.setCellValue(doubleValue);
            } else if (value instanceof Long) {
                Long longValue = (Long) value;
                cell.setCellValue(longValue);
            }
            else {
                cell.setCellValue(value.toString());
            }
        }
    }
}