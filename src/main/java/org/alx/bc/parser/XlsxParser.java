package org.alx.bc.parser;

import org.alx.bc.dto.PropertiesBundle;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XlsxParser {


    public PropertiesBundle parse(InputStream xlsxFile) {

        try {
            Workbook wb = WorkbookFactory.create(xlsxFile);
            Sheet sheet = wb.getSheetAt(0);

            List<String> valueAliases = new ArrayList<>();
            Row headerRow = sheet.getRow(0);
            for (int i = 1; i < headerRow.getLastCellNum(); i++) {
                Cell headerCell = headerRow.getCell(i);
                String headerText = headerCell.getStringCellValue();
                if(headerText != null && !headerText.isEmpty()) {
                    valueAliases.add(headerText);
                } else {
                    break;
                }
            }

            LinkedHashMap<String, Map<String, String>> values = new LinkedHashMap<>();
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row propertyRow = sheet.getRow(i);
                Cell propertyNameCell = propertyRow.getCell(0);
                String propertyName = propertyNameCell.getStringCellValue();
                if(propertyName == null || propertyName.isEmpty()) {
                    break;
                }
                for (int j = 1; j < valueAliases.size() + 1; j++) {
                    String valueAlias = valueAliases.get(j - 1);
                    Cell propertyValueCell = propertyRow.getCell(j);
                    String propertyValue = propertyValueCell.getStringCellValue();

                    values.computeIfAbsent(propertyName, key -> new LinkedHashMap<>())
                          .put(valueAlias, propertyValue);

                }
            }

            return new PropertiesBundle(sheet.getSheetName(), valueAliases.toArray(new String[0]), values);

        } catch (IOException e) {
            throw new IllegalStateException("Unable to parse xlsx file", e);
        }
    }

}
