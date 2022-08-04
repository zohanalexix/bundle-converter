//package com.ptx.test.util;
//
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Iterator;
//
///**
// * Used to compare values from two Xlsx files
// */
//public class XlsxComparator {
//
//
//    public void checkFilesHaveSameValues(File file1, File file2) throws ComparisonException {
//
//        try {
//            XSSFWorkbook workbook1 = new XSSFWorkbook(file1);
//            XSSFWorkbook workbook2 = new XSSFWorkbook(file2);
//
//            int workbook1SheetCount = workbook1.getNumberOfSheets();
//            int workbook2SheetCount = workbook2.getNumberOfSheets();
//            if (workbook1SheetCount != workbook2SheetCount) {
//                throw new ComparisonException("Different number of sheets. " +
//                        workbook1SheetCount + " vs " + workbook2SheetCount);
//            }
//
//            for (int i = 0; i < workbook1SheetCount; i++) {
//                XSSFSheet workbook1Sheet = workbook1.getSheetAt(i);
//                XSSFSheet workbook2Sheet = workbook2.getSheetAt(i);
//                checkSheetsHaveSameValues(workbook1Sheet, workbook2Sheet);
//            }
//
//        } catch (IOException | InvalidFormatException e) {
//            throw new ComparisonException("Unable to compare files due to " + e.getClass().getSimpleName(), e);
//        }
//    }
//
//
//    private void checkSheetsHaveSameValues(XSSFSheet sheet1, XSSFSheet sheet2) throws ComparisonException {
//        Iterator<Row> rowIterator1 = sheet1.rowIterator();
//        Iterator<Row> rowIterator2 = sheet2.rowIterator();
//
//        while(rowIterator1.hasNext()) {
//            if(!rowIterator2.hasNext()) {
//                throw new ComparisonException("sheet1 has more rows than sheet2");
//            }
//            Row sheet1Row = rowIterator1.next();
//            Row sheet2Row = rowIterator2.next();
//
//            sheet1Row.
//
//        }
//
//
//    }
//
//
//}
