package com.ptx.service;

import org.apache.commons.collections4.properties.SortedProperties;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.util.*;

public class PropertiesToXlsxService {



    public byte[] generateXlsx(String bundleName, File[] propertyFiles) throws IOException {

        //key1=property name, key2=file name, value=cell text
        Map<String, Map<String, String>> propertiesAndValues = new LinkedHashMap<>();

        //parse all properties file and populate our Map
        List<String> fileAliases = new ArrayList<>();
        for (File propertyFile : propertyFiles) {
            String fileAlias = propertyFile.getName().replace(bundleName, "")
                    .replace(".properties", "");
            if(fileAlias.startsWith("_")) {
                fileAlias = fileAlias.substring(1);
            }

            fileAliases.add(fileAlias);
            parsePropertyFile(propertiesAndValues, propertyFile, fileAlias);
        }

        return createXlsx(bundleName, propertiesAndValues, fileAliases);
    }

    private byte[] createXlsx(String sheetName, Map<String, Map<String, String>> propertiesAndValues, List<String> fileAliases) {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        XSSFFont fontBold = workbook.createFont();
        fontBold.setBold(true);
        XSSFCellStyle styleBold = workbook.createCellStyle();
        styleBold.setFont(fontBold);

        // Header row
        int rowIdx=0;
        XSSFRow headerRow = sheet.createRow(rowIdx++);
        for (int i=0; i<fileAliases.size(); i++) {
            String fileAlias = fileAliases.get(i);
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
            for (int i=0; i<fileAliases.size(); i++) {
                String fileAlias = fileAliases.get(i);
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

    private void parsePropertyFile(Map<String, Map<String, String>> propertiesAndValues, File propertyFile, String fileAlias)
            throws IOException {

        SortedProperties prop = new SortedProperties();
        prop.load(new FileInputStream(propertyFile));

        Iterator<Object> propIterator = prop.keys().asIterator();
        while(propIterator.hasNext()) {
            Object key = propIterator.next();
            Object value = prop.get(key);

            propertiesAndValues
                    .computeIfAbsent(String.valueOf(key), key2 -> new LinkedHashMap<>())
                    .put(fileAlias, String.valueOf(value));
        }

    }



}
