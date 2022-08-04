package com.ptx.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesToXlsxService {



    public byte[] generateXlsx(String bundleName, File[] propertyFiles) throws IOException {

        //key1=property name, key2=file name
        Map<String, Map<String, String>> propertiesAndValues = new LinkedHashMap<>();

        //parse all properties file and populate our Map
        for (File propertyFile : propertyFiles) {
            parsePropertyFile(propertiesAndValues, propertyFile);
        }



        throw new UnsupportedOperationException();
    }

    private void parsePropertyFile(Map<String, Map<String, String>> propertiesAndValues, File propertyFile) throws IOException {
        String fileName = propertyFile.getName();

        Properties prop = new Properties();
        prop.load(new FileInputStream(propertyFile));

        prop.forEach((key, value) -> propertiesAndValues
                .computeIfAbsent(String.valueOf(key), key2 -> new LinkedHashMap<>())
                .put(fileName, String.valueOf(value)));
    }

    private byte[] generateXlsx(Map<String, Map<String, String>> propertiesAndValues, String bundleName) throws IOException {

        try(XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(bundleName);

            for (Map.Entry<String, Map<String, String>> propertyAndValues : propertiesAndValues.entrySet()) {

                String property = propertyAndValues.getKey();
                Map<String, String> values = propertyAndValues.getValue();

                //TODO: Continue


            }
        }

        throw new UnsupportedOperationException();
    }


}
