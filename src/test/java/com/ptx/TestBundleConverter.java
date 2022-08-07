package com.ptx;

import bad.robot.excel.matchers.WorkbookMatcher;
import org.apache.commons.collections4.properties.SortedProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class TestBundleConverter {


    public static final String TEST_BUNDLE_FOLDER = "data";
    public static final String TEST_BUNDLE_NAME   = "messages";
    public static final String TEST_OUTPUT_FOLDER = "build/test-result";

    public static final String EXPECTED_RESULT = "data/messages.xlsx"; //predefined file with expected values


    private final BundleConverter bundleConverter = new BundleConverter();

    @Test
    public void convertPropertiesToXlsx() throws IOException, InvalidFormatException {

        File outputFolder = new File(TEST_OUTPUT_FOLDER);
        File[] propertyFiles = getTestPropertyFiles();
        File expectedOutput = getFile(EXPECTED_RESULT);

        bundleConverter.convertPropertiesToXlsx(TEST_BUNDLE_NAME, propertyFiles, outputFolder);

        File createdFile = getFile(outputFolder, TEST_BUNDLE_NAME + ".xlsx");
        assertTrue(createdFile.exists());

        XSSFWorkbook outcome = new XSSFWorkbook(createdFile);


        XSSFWorkbook expectedWorkbook = new XSSFWorkbook(expectedOutput);

        Matcher<Workbook> workbookMatcher = WorkbookMatcher.sameWorkbook(expectedWorkbook);
        boolean matches = workbookMatcher.matches(outcome);
        assertTrue(matches);
    }



    @Test
    public void convertXlsxToProperties() throws IOException {

        File outputFolder = new File(TEST_OUTPUT_FOLDER);
        File xlsxFile = getFile(EXPECTED_RESULT);
        File[] expectedOutput = getTestPropertyFiles();

        bundleConverter.convertXlsxToProperties(xlsxFile, outputFolder);

        String[] generatedFiles = outputFolder.list((dir, name) -> name.endsWith(".properties"));

        assertNotNull(generatedFiles);

        for (File expectedPropertiesFile : expectedOutput) {
            File outputPropertiesFile = getFile(outputFolder, expectedPropertiesFile.getName());

            SortedProperties expectedProperties = new SortedProperties();
            expectedProperties.load(new FileInputStream(expectedPropertiesFile));

            SortedProperties actualProperties = new SortedProperties();
            actualProperties.load(new FileInputStream(outputPropertiesFile));

            assertEquals(expectedProperties.size(), actualProperties.size());

            for (Object expectedKey : expectedProperties.keySet()) {
                Object expectedValue = expectedProperties.get(expectedKey);
                Object actualValue = actualProperties.get(expectedKey);
                assertEquals(expectedValue, actualValue);
            }
        }

    }


    private File[] getTestPropertyFiles() {
        File dataFolder = getFile(TEST_BUNDLE_FOLDER);
        File[] propertyFiles = dataFolder.listFiles((file, name) -> name.startsWith(TEST_BUNDLE_NAME) &&
                name.endsWith(".properties"));
        if(propertyFiles == null || propertyFiles.length == 0) {
            throw new IllegalStateException("No test files found matching '" + TEST_BUNDLE_FOLDER + "/" +
                    TEST_BUNDLE_NAME + "**.properties");
        }
        return propertyFiles;
    }


    private File getFile(String testBundleFolder) {
        URL fileUrl = Objects.requireNonNull(getClass().getClassLoader().getResource(testBundleFolder));
        return new File(fileUrl.getFile());
    }


    private File getFile(File folder, String fileName) throws IOException {
        File[] files = folder.listFiles((dir, name) -> name.equals(fileName));
        if(files == null || files.length == 0) {
            throw new IOException("No file called '" + fileName + "' found in " + folder.getPath());
        }
        return files[0];
    }

}
