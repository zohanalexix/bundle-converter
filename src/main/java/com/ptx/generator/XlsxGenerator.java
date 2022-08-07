package com.ptx.generator;

import com.ptx.dto.PropertiesBundle;
import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class XlsxGenerator {


    public byte[] generateXlsx(PropertiesBundle propertiesBundle) {
        return createXlsx(
                propertiesBundle.getBundleName(),
                propertiesBundle.getValues(),
                propertiesBundle.getValueAliases());

    }

    private byte[] createXlsx(String sheetName, Map<String, Map<String, String>> propertiesAndValues, String[] fileAliases) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        XSSFFont fontBold = workbook.createFont();
        fontBold.setBold(true);
        XSSFCellStyle styleBold = workbook.createCellStyle();
        styleBold.setFont(fontBold);

        // Header row
        int rowIdx=0;
        XSSFRow headerRow = sheet.createRow(rowIdx++);
        for (int i=0; i<fileAliases.length; i++) {
            String fileAlias = fileAliases[i];
            XSSFCell headerCell = headerRow.createCell(i + 1);
            headerCell.setCellValue(fileAlias);
            headerCell.setCellStyle(styleBold);
        }

        // Values
        for (Map.Entry<String, Map<String, String>> propertiesRow : propertiesAndValues.entrySet()) {
            XSSFRow row = sheet.createRow(rowIdx++);

            String propertyName = propertiesRow.getKey();
            XSSFCell propertyNameCell = row.createCell(0);
            propertyNameCell.setCellValue(propertyName);

            Map<String, String> values = propertiesRow.getValue();
            for (int i=0; i<fileAliases.length; i++) {
                String fileAlias = fileAliases[i];
                String textForFile = Objects.requireNonNullElse(values.get(fileAlias), "");

                XSSFCell valueCell = row.createCell(i + 1);
                valueCell.setCellValue(textForFile);
            }
        }

        //add 1 blank row at the end
        sheet.createRow(rowIdx);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e ) {
            throw new IllegalStateException("Unable to write workbook to output stream", e);
        }
    }

}
