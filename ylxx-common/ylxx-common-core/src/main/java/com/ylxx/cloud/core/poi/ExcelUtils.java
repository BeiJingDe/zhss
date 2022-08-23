package com.ylxx.cloud.core.poi;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;


/**
 * Excel导入导出
 */
public class ExcelUtils {


    public static void exportExcel(HttpServletResponse response, String[] header, String[] keys, Class clazz,List<? extends Object> content, String title, String sheetName,Integer totalNum,Double totalSum){

        OutputStream out = null;
        try {
        Workbook wb = new SXSSFWorkbook(1000);
        Sheet sheet = wb.createSheet(sheetName);

        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        CellRangeAddress c1 = new CellRangeAddress(0, 0, 0, header.length-1);
        sheet.addMergedRegion(c1);
        CellRangeAddress c2 = new CellRangeAddress(1, 1, 0, header.length-1);
        sheet.addMergedRegion(c2);
        //标题行
        Row titleRow = sheet.createRow( 0);
        titleRow.setHeight((short) 700);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle(wb));
        //总计行
        Row rower = sheet.createRow(1);
        Cell celler = rower.createCell(0);
        StringBuilder sb = new StringBuilder();
        if(ObjectUtil.isNotNull(totalNum)){
            sb.append("数量：").append(totalNum)  .append("件 ") ;
        }
        if(ObjectUtil.isNotNull(totalSum)){
            sb.append("金额：").append(totalSum)  .append("万元 ") ;
        }
        celler.setCellValue(sb.toString());
        celler.setCellStyle(totalStyle(wb));

        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个

        //加载单元格样式
        for (int i = 0; i < header.length; i++) {
            sheet.setColumnWidth(i, 20 * 256);
        }

        Row headRow = sheet.createRow(2);
        for (int i = 0; i < header.length; i++) {
            Cell cell = headRow.createCell(i);
            cell.setCellValue(header[i]);
            cell.setCellStyle(headerStyle(wb));
        }


        Method[]  methods = ReflectUtil.getMethods(clazz);
        for (int k = 0; k <content.size() ; k++) {
            Row row = sheet.createRow((int) k + 3);
            row.setHeight((short) 500);
             for (int i = 0; i <methods.length ; i++) {
                for (int j = 0; j <keys.length ; j++) {
                if(StrUtil.containsAnyIgnoreCase(methods[i].getName(),keys[j]) && StrUtil.containsAnyIgnoreCase(methods[i].getName(),"get")){
//                    System.out.println(methods[i].getName()+"====="+keys[j]);
                    Cell cell = row.createCell(j);
                    Object obj = ReflectUtil.invoke(content.get(k),methods[i]);
                     if (obj instanceof Integer) {
                         cell.setCellValue(obj == null ? 0 : Integer.parseInt(obj.toString()));
                    } else if (obj instanceof Double) {
                         cell.setCellValue(obj == null ?  0.0: Double.parseDouble(obj.toString()));
                    }else{
                         cell.setCellValue(obj == null ? "" : obj.toString());
                    }
                    cell.setCellStyle(contentStyle(wb));
                    }
                }
            }
        }
        title = title + ".xlsx";
        response.reset();
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
//        response.setHeader("Content-Disposition", "attachment; filename=" + title);
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(title, "utf-8"));
        wb.write(response.getOutputStream());
        out  = response.getOutputStream();
        out.flush();
        } catch (Exception e) {
            throw new ServiceException("导出Excel失败，请联系网站管理员！", e);
        } finally {
            IoUtil.close(out);
        }
    }

    /**
     * 标题样式
     */
    private  static CellStyle titleStyle(Workbook wb){
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);// 自动换行
        style.setFont(font);
        return style;
    }

    /**
     * 总计行样式
     */
    private static CellStyle totalStyle(Workbook wb){
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 13);
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
//        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
//        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setFillForegroundColor(IndexedColors.RED.getIndex());
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);// 自动换行
        style.setFont(font);
        return style;
    }

    /**
     * 表头样式
     */
    private static CellStyle headerStyle(Workbook wb){
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 11);
        CellStyle cellStyle = commonStyle(wb);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 内容样式
     */
    private static CellStyle contentStyle(Workbook wb){
        Font font = wb.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 10);
        CellStyle cellStyle = commonStyle(wb);
        cellStyle.setFont(font);
        return cellStyle;
    }

    /**
     * 公共样式
     */
    private static CellStyle commonStyle(Workbook wb){
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
//        style.setBorderBottom(BorderStyle.THIN);
//        style.setBorderLeft(BorderStyle.THIN);
//        style.setBorderTop(BorderStyle.THIN);
//        style.setBorderRight(BorderStyle.THIN);
        style.setWrapText(true);// 自动换行
        return style;
    }

    /**
     *
     * @Title: setRegionStyle
     * @Description: TODO(合并单元格后边框不显示问题)
     * @author: GMY
     * @date: 2018年5月10日 上午10:46:00
     * @param @param sheet
     * @param @param region
     * @param @param cs    设定文件
     * @return void    返回类型
     * @throws
     */
    public static void setRegionStyle(Sheet sheet, CellRangeAddress region, CellStyle cs) {
        for (int i = region.getFirstRow(); i <= region.getLastRow(); i++) {
            Row row = CellUtil.getRow(i, sheet);
            for (int j = region.getFirstColumn(); j <= region.getLastColumn(); j++) {
                Cell cell = CellUtil.getCell(row, (short) j);
                cell.setCellStyle(cs);
            }
        }
    }

    /**
     * 设置合并单元格边框 - 线条
     * */
    private static void setBorderStyle(Sheet sheet, CellRangeAddress region) {
        // 合并单元格左边框样式
        RegionUtil.setBorderLeft(BorderStyle.THICK, region, sheet);
        RegionUtil.setLeftBorderColor(IndexedColors.BLUE.getIndex(), region, sheet);

        // 合并单元格上边框样式
        RegionUtil.setBorderTop(BorderStyle.DASH_DOT_DOT, region, sheet);
        RegionUtil.setTopBorderColor(IndexedColors.LIGHT_ORANGE.getIndex(), region, sheet);

        // 合并单元格右边框样式
        RegionUtil.setBorderRight(BorderStyle.SLANTED_DASH_DOT, region, sheet);
        RegionUtil.setRightBorderColor(IndexedColors.RED.getIndex(), region, sheet);

        // 合并单元格下边框样式
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM_DASHED, region, sheet);
        RegionUtil.setBottomBorderColor(IndexedColors.GREEN.getIndex(), region, sheet);
    }
}
