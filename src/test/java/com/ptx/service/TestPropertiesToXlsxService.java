package com.ptx.service;

import bad.robot.excel.matchers.WorkbookMatcher;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestPropertiesToXlsxService {


    public static final String TEST_BUNDLE_FOLDER = "data";
    public static final String TEST_BUNDLE_NAME = "messages";

    public static final String EXPECTED_RESULT = "data/messages.xlsx"; //predefined file with expected values


    private final PropertiesToXlsxService propertiesToXlsxService = new PropertiesToXlsxService();

    @Test
    public void generateXlsx() throws IOException, InvalidFormatException {

        File dataFolder = getFile(TEST_BUNDLE_FOLDER);
        File[] propertyFiles = dataFolder.listFiles((file, name) -> name.startsWith(TEST_BUNDLE_NAME) &&
                name.endsWith(".properties"));
        if(propertyFiles == null || propertyFiles.length == 0) {
            throw new IllegalStateException("No test files found matching '" + TEST_BUNDLE_FOLDER + "/" +
                    TEST_BUNDLE_NAME + "**.properties");
        }

        byte[] xlsxFile = propertiesToXlsxService.generateXlsx(TEST_BUNDLE_NAME, propertyFiles);
        XSSFWorkbook outcome = new XSSFWorkbook(new ByteArrayInputStream(xlsxFile));


        File expectedFile = getFile(EXPECTED_RESULT);
        XSSFWorkbook expectedWorkbook = new XSSFWorkbook(expectedFile);

        assertTrue(WorkbookMatcher.sameWorkbook(expectedWorkbook).matches(outcome));

    }

    private File getFile(String testBundleFolder) {
        URL fileUrl = Objects.requireNonNull(getClass().getClassLoader().getResource(testBundleFolder));
        return new File(fileUrl.getFile());
    }


}
