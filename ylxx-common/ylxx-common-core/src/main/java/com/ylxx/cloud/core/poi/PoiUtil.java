package com.ylxx.cloud.core.poi;

import cn.hutool.core.io.IoUtil;
import com.ylxx.cloud.exception.ext.ServiceException;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;

import java.io.*;
import java.text.ParseException;
import java.util.Map;

public class PoiUtil {

    private static String EXCEL_HIDE_SHEET_NAME = "poihide";
    private static String HIDE_SHEET_NAME_PROVINCE = "materialList";
    private static int startRow = 1; // 下拉的开始行
    private static int endRow = 1000; // 下拉的结束行

    /**
     * 生成联动下拉框
     * @param workBook
     * @param fatherMap
     * @param subMap
     * @throws ParseException
     */
    public static void createCascleData(Workbook workBook,Map<String,String>fatherMap, Map<String,Map<String,String>> subMap,int fatherCol,int sonCol) {

        //生成第二个sheet，用来存放下拉内容
        creatDataSheet(workBook,fatherMap,  subMap);
        //生成数据验证
        setDataValidation(workBook,fatherCol,sonCol);
    }


    //生成sheet，用来存放下拉内容
    private static void creatDataSheet(Workbook workbook ,Map<String,String>fatherMap, Map<String,Map<String,String>> subMap) {
        Sheet hideInfoSheet = workbook.createSheet(EXCEL_HIDE_SHEET_NAME);//隐藏一些信息

        int rowId = 1;
        //第二行设置物资来源
        Row provinceNameRow = hideInfoSheet.createRow(rowId++);
        creatRow(provinceNameRow, fatherMap);
        creatExcelNameList(workbook, HIDE_SHEET_NAME_PROVINCE, rowId, fatherMap.size(), false);
        //以下行存储点列表
        for(Map.Entry<String, String> entry : fatherMap.entrySet()){
            String mapKey =  entry.getKey();
            Map<String,String> sonMap = subMap.get(mapKey);
            String mapValue = entry.getValue();
            Row cityNameRow = hideInfoSheet.createRow(rowId++);
            creatRow(cityNameRow, sonMap);

            creatExcelNameList(workbook, mapValue, rowId, sonMap.size(), true);
        }

        //设置隐藏页标志
        workbook.setSheetHidden(workbook.getSheetIndex(EXCEL_HIDE_SHEET_NAME), true);
    }

    private static void creatRow(Row currentRow, Map<String,String> map) {
        if(!map.isEmpty()){
            int i = 0;
            for(String value:map.values()){

                Cell userNameLableCell = currentRow.createCell(i++);
                userNameLableCell.setCellValue(value);

            }
        }

    }

    private static void creatExcelNameList(Workbook workbook, String nameCode, int order, int size, boolean cascadeFlag) {
        Name name;
        name = workbook.createName();
        name.setNameName(nameCode);
        String formula = EXCEL_HIDE_SHEET_NAME + "!" + creatExcelNameList(order, size, cascadeFlag);
        // System.out.println(nameCode + " ==  " + formula);
        name.setRefersToFormula(formula);
    }

    private static String creatExcelNameList(int order, int size, boolean cascadeFlag) {
        char start = 'A';
        if (cascadeFlag) {
            if (size <= 25) {
                char end = (char) (start + size - 1);
                return "$" + start + "$" + order + ":$" + end + "$" + order;
            } else {
                char endPrefix = 'A';
                char endSuffix = 'A';
                if ((size - 25) / 26 == 0 || size == 51) {//26-51之间，包括边界（仅两次字母表计算）
                    if ((size - 25) % 26 == 0) {//边界值
                        endSuffix = (char) ('A' + 25);
                    } else {
                        endSuffix = (char) ('A' + (size - 25) % 26 - 1);
                    }
                } else {//51以上
                    if ((size - 25) % 26 == 0) {
                        endSuffix = (char) ('A' + 25);
                        endPrefix = (char) (endPrefix + (size - 25) / 26 - 1);
                    } else {
                        endSuffix = (char) ('A' + (size - 25) % 26 - 1);
                        endPrefix = (char) (endPrefix + (size - 25) / 26);
                    }
                }
                return "$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
            }
        } else {
            if (size <= 26) {
                char end = (char) (start + size - 1);
                return "$" + start + "$" + order + ":$" + end + "$" + order;
            } else {
                char endPrefix = 'A';
                char endSuffix = 'A';
                if (size % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    if (size > 52 && size / 26 > 0) {
                        endPrefix = (char) (endPrefix + size / 26 - 2);
                    }
                } else {
                    endSuffix = (char) ('A' + size % 26 - 1);
                    if (size > 52 && size / 26 > 0) {
                        endPrefix = (char) (endPrefix + size / 26 - 1);
                    }
                }
                return "$" + start + "$" + order + ":$" + endPrefix + endSuffix + "$" + order;
            }
        }
    }


    public static void setDataValidation(Workbook wb,int fatherCol,int sonCol) {
        int sheetIndex = wb.getNumberOfSheets();
        Sheet sheet = wb.getSheetAt(0);
        if (!EXCEL_HIDE_SHEET_NAME.equals(sheet.getSheetName())) {
            //物资来源选项添加验证数据
            for (int a = startRow; a < endRow; a++) {
                DataValidation data_validation_list2 = getDataValidationByFormula(HIDE_SHEET_NAME_PROVINCE, a, fatherCol,sheet);
                sheet.addValidationData(data_validation_list2);
                //存储点选项添加验证数据
                DataValidation data_validation_list3 = getDataValidationByFormula("INDIRECT($"+((char)('A'+fatherCol-1))+"$" + (a+1) + ")", a, sonCol,sheet);
                sheet.addValidationData(data_validation_list3);
            }

        }
    }

    private static DataValidation getDataValidationByFormula(String formulaString, int naturalRowIndex, int naturalColumnIndex, Sheet sheet) {
        //System.out.println("formulaString  " + formulaString);
        //加载下拉列表内容

        DataValidationHelper helper = sheet.getDataValidationHelper();
        //XSSFDataValidationConstraint  constraint = (XSSFDataValidationConstraint)dvHelper.createFormulaListConstraint(formulaString);
        DataValidationConstraint constraint = helper.createFormulaListConstraint(formulaString);
        //设置数据有效性加载在哪个单元格上。
        //四个参数分别是：起始行、终止行、起始列、终止列
        int firstRow = naturalRowIndex ;
        int lastRow = naturalRowIndex;
        int firstCol = naturalColumnIndex - 1;
        int lastCol = naturalColumnIndex - 1;
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);         //数据有效性对象
        //DataValidation data_validation_list = new HSSFDataValidation(regions, constraint);
         /*
        data_validation_list.setEmptyCellAllowed(false);

        if (data_validation_list instanceof XSSFDataValidation) {
            data_validation_list.setSuppressDropDownArrow(true);
            //data_validation_list.setShowErrorBox(true);
        } else {
            data_validation_list.setSuppressDropDownArrow(false);
        }

       //设置输入信息提示信息
        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        //设置输入错误提示信息
        data_validation_list.createErrorBox("选择错误提示", "你输入的值未在备选列表中，请下拉选择合适的值！");
        */
        return dataValidation;
    }

    /**
     * 创建下拉选项
     * @param datas
     * @param book
     */
    public static void createExcelSelect(String[] datas ,HSSFWorkbook book){
        HSSFSheet sheet = book.getSheetAt(0);//
        int cellNum = 0;
        String  hiddenSheet = "nameHidden";
        DVConstraint constraint = null;
        CellRangeAddressList addressList = null;
        HSSFDataValidation validation = null; // 数据验证
        HSSFSheet category1Hidden = book.createSheet(hiddenSheet); // 创建隐藏域

        for (int i=0;  i< datas.length; i++) { // 循环赋值（为了防止下拉框的行数与隐藏域的行数相对应来获取>=选中行数的数组，将隐藏域加到结束行之后）
            category1Hidden.createRow(endRow + i).createCell(cellNum).setCellValue(datas[i]);
        }
        Name category1Name = book.createName();
        category1Name.setNameName(hiddenSheet);
        category1Name.setRefersToFormula(hiddenSheet + "!A1:A" + (datas.length + endRow)); // A1:A代表隐藏域创建第?列createCell(?)时。以A1列开始A行数据获取下拉数组

        constraint = DVConstraint.createFormulaListConstraint(hiddenSheet);
        addressList = new CellRangeAddressList(startRow, endRow, cellNum, cellNum);
        validation = new HSSFDataValidation(addressList, constraint);
        // // 1隐藏
        book.setSheetHidden(book.getSheetIndex(hiddenSheet), true);
        sheet.addValidationData(validation);
    }

    /**
     * word添加水印
     * @param watermark：水印文字
     */
    public static void createWatermark(String srcDoc, String destDoc, String watermark) {
        InputStream in = null;
        OutputStream os = null;
        XWPFDocument doc = null;
        try {
            in = new FileInputStream(new File(srcDoc));
            os = new FileOutputStream(new File(destDoc));
            doc= new XWPFDocument(in);
            XWPFParagraph paragraph = doc.createParagraph();

            // 含有页眉时 使用这种方式不会报错
            CTSectPr sectPr=doc.getDocument().getBody().addNewSectPr();
            XWPFHeaderFooterPolicy headerFooterPolicy = new XWPFHeaderFooterPolicy (doc,sectPr);
            headerFooterPolicy.createWatermark(watermark);

            XWPFHeader header = headerFooterPolicy.getHeader(XWPFHeaderFooterPolicy.DEFAULT);
            paragraph = header.getParagraphArray(0);

            org.apache.xmlbeans.XmlObject[] xmlobjects = paragraph.getCTP().getRArray(0).getPictArray(0).selectChildren(
                    new javax.xml.namespace.QName("urn:schemas-microsoft-com:vml", "shape"));

            if (xmlobjects.length > 0) {
                com.microsoft.schemas.vml.CTShape ctshape = (com.microsoft.schemas.vml.CTShape)xmlobjects[0];
                ctshape.setFillcolor("#d8d8d8");
                ctshape.setStyle(ctshape.getStyle() + ";rotation:315");
            }
            doc.write(os);
            doc.close();
        } catch (Exception e) {
            throw new ServiceException("word文件添加水印失败", e);
        } finally {
            IoUtil.close(doc);
            IoUtil.close(in);
            IoUtil.close(os);
        }
    }

}
