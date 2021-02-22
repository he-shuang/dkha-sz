

package com.dkha.commons.tools.excel;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.dkha.commons.tools.excel.ExcelSheetVO;
import com.dkha.commons.tools.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Excel工具类
 * @since 1.0.0
 */
public class ExcelUtils {

    /**
     * Excel导出
     *
     * @param response      response
     * @param fileName      文件名
     * @param list          数据List
     * @param pojoClass     对象Class
     */
    public static void exportExcel(HttpServletResponse response, String fileName, Collection<?> list,
                                   Class<?> pojoClass) throws IOException {
        if(StringUtils.isBlank(fileName)){
            //当前日期
            fileName = DateUtils.format(new Date());
        }

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, list);
        Sheet sheet1 = workbook.getSheetAt(0);
        sheet1.setDefaultColumnWidth(50*256);
        sheet1.setDefaultRowHeight((short)(2*256));
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition",
                "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.flush();
        out.close();
    }

    /**
     * Excel导出，先sourceList转换成List<targetClass>，再导出
     *
     * @param response      response
     * @param fileName      文件名
     * @param sourceList    原数据List
     * @param targetClass   目标对象Class
     */
    public static void exportExcelToTarget(HttpServletResponse response, String fileName, Collection<?> sourceList,
                                           Class<?> targetClass) throws Exception {
        List<Object> targetList = new ArrayList<>(sourceList.size());
        for(Object source : sourceList){
            Object target = targetClass.newInstance();
            BeanUtils.copyProperties(source, target);
            targetList.add(target);
        }

        exportExcel(response, fileName, targetList, targetClass);
    }


    /**
     * 导入
     */
    public static ExcelSheetVO importExcel(InputStream is, String suffix) {
        Workbook wb = null;
        try {

            if (ExcelVersion.V2003.getSuffix().equals(suffix)) {
                wb = new HSSFWorkbook(is);

            } else if (ExcelVersion.V2007.getSuffix().equals(suffix)) {
                wb = new XSSFWorkbook(is);

            } else {
                // 无效后缀名称，这里之能保证excel的后缀名称，不能保证文件类型正确，不过没关系，在创建Workbook的时候会校验文件格式
                throw new IllegalArgumentException("Invalid excel version");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        if (wb != null) {
            String[] headers = readExcelTitle(wb);
            List<List<Object>> dataList = readExcelContent(wb);
            excelSheetVO.setHeaders(headers);
            excelSheetVO.setDataList(dataList);
        }

        return excelSheetVO;
    }

    /**
     * 读取Excel表格表头的内容
     * @return String 表头内容的数组
     */
    private static String[] readExcelTitle(Workbook wb) {

        Sheet sheet = wb.getSheetAt(0);
        //得到首行的row
        Row row = sheet.getRow(1);
        // 标题总列数
        int colNum = row.getPhysicalNumberOfCells();
        String[] title = new String[colNum];
        for (int i = 0; i < colNum; i++) {
            title[i] = getCellFormatValue(row.getCell((short) i));
        }
        return title;
    }

    /**
     * 读取Excel数据内容
     * @return Map 包含单元格数据内容的Map对象
     */
    private static List<List<Object>> readExcelContent(Workbook wb) {
        List<List<Object>> dataList = new ArrayList<>();
        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        //由于第0行和第一行已经合并了  在这里索引从2开始
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 2; i <= rowNum; i++) {
            List<Object> data = new ArrayList<>();
            row = sheet.getRow(i);
            int j = 0;
            if(row != null){
                while (j < colNum) {
                    String trim = getCellFormatValue(row.getCell((short) j)).trim();
                    data.add(trim);
                    j++;
                }
                dataList.add(data);
            }

        }
        return dataList;
    }

    /**
     * 根据Cell类型设置数据
     */
    private static String getCellFormatValue(Cell cell) {
        String cellvalue = "";
        if (cell != null) {
            CellType cellType = cell.getCellType();
            // 判断当前Cell的Type
            switch (cellType) {
                // 如果当前Cell的Type为NUMERIC
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        cellvalue = DateUtils.format(date,DateUtils.DATE_TIME_PATTERN);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        DecimalFormat df = new DecimalFormat("0");
                        cellvalue = df.format(cell.getNumericCellValue());
                    }
                    break;
                // 如果当前Cell的Type为STRIN
                case STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

    public static void main(String[] args) {
        try {

            // 对读取Excel表格内容测试
            File is2 = new File("C:\\Users\\Administrator\\Desktop\\服务器.xlsx");
            InputStream fileInputStream = new FileInputStream(is2);
            ExcelSheetVO excelSheetVO = ExcelUtils.importExcel(fileInputStream, "xlsx");
            System.out.println("获得Excel表格的内容:");
            //这里由于xls合并了单元格需要对索引特殊处理
            System.out.println(JSONObject.toJSONString(excelSheetVO));

        } catch (Exception e) {
            System.out.println("未找到指定路径的文件!");
            e.printStackTrace();
        }
    }
}
