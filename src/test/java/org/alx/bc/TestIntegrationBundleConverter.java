package org.alx.bc;

import bad.robot.excel.matchers.WorkbookMatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.properties.SortedProperties;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class TestIntegrationBundleConverter {


    public static final String TEST_BUNDLE_FOLDER = "data";
    public static final String TEST_BUNDLE_NAME   = "messages";
    public static final String TEST_OUTPUT_FOLDER = "build/test-result";

    public static final String TEST_DATA_XLSX = "data/messages.xlsx"; //predefined file with expected values


    private final BundleConverter bundleConverter = new BundleConverter();

    @Test
    public void convertPropertiesToXlsx() throws IOException, InvalidFormatException {

        File outputFolder = new File(TEST_OUTPUT_FOLDER);
        File propertyFiles = getFile(TEST_BUNDLE_FOLDER);
        File expectedOutput = getFile(TEST_DATA_XLSX);

        bundleConverter.convertPropertiesToXlsx(propertyFiles, TEST_BUNDLE_NAME, outputFolder);

        File createdFile = getFile(outputFolder, TEST_BUNDLE_NAME + ".xlsx");
        assertTrue(createdFile.exists());

        XSSFWorkbook outcome = new XSSFWorkbook(createdFile);
        XSSFWorkbook expectedWorkbook = new XSSFWorkbook(expectedOutput);

        Matcher<Workbook> workbookMatcher = WorkbookMatcher.sameWorkbook(expectedWorkbook);
        boolean matches = workbookMatcher.matches(outcome);
        assertTrue(matches);
    }

    @Test
    public void convertJsonPropertiesToXlsx() throws IOException, InvalidFormatException {
        File outputFolder = new File(TEST_OUTPUT_FOLDER);
        File propertyFiles = getFile(TEST_BUNDLE_FOLDER);
        File expectedOutput = getFile(TEST_DATA_XLSX);

        bundleConverter.convertJsonPropertiesToXlsx(propertyFiles, TEST_BUNDLE_NAME, outputFolder);

        File createdFile = getFile(outputFolder, TEST_BUNDLE_NAME + ".xlsx");
        assertTrue(createdFile.exists());

        XSSFWorkbook outcome = new XSSFWorkbook(createdFile);
        XSSFWorkbook expectedWorkbook = new XSSFWorkbook(expectedOutput);

        Matcher<Workbook> workbookMatcher = WorkbookMatcher.sameWorkbook(expectedWorkbook);
        StringDescription description = new StringDescription();
        workbookMatcher.describeTo(description);
        boolean matches = workbookMatcher.matches(outcome);
        if(!matches) {
            log.info(description.toString());
        }
        assertTrue(matches);
    }



    @Test
    public void convertXlsxToProperties() throws IOException {

        File outputFolder = new File(TEST_OUTPUT_FOLDER);
        File xlsxFile = getFile(TEST_DATA_XLSX);
        File[] expectedOutput = getTestPropertyFiles();

        bundleConverter.convertXlsxToProperties(new FileInputStream(xlsxFile), outputFolder);

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

    @Test
    public void convertXlsxToJsonProperties() throws IOException, ParseException {
        File outputFolder = new File(TEST_OUTPUT_FOLDER);
        File xlsxFile = getFile(TEST_DATA_XLSX);
        File[] expectedOutput = getTestJsonFiles();

        bundleConverter.convertXlsxToJsonProperties(new FileInputStream(xlsxFile), outputFolder);

        String[] generatedFiles = outputFolder.list((dir, name) -> name.endsWith(".json"));

        assertNotNull(generatedFiles);

        for (File expectedPropertiesFile : expectedOutput) {

            String actualJson = Files.readString(getFile(outputFolder, expectedPropertiesFile.getName()).toPath());
            String expectedJson = Files.readString(expectedPropertiesFile.toPath());

            JSONObject actualJsonObject = (JSONObject) new JSONParser().parse(actualJson);
            JSONObject expectedJsonObject = (JSONObject) new JSONParser().parse(expectedJson);

            assertEquals(expectedJsonObject.keySet().size(), actualJsonObject.keySet().size());

            for (Object expectedKey : expectedJsonObject.keySet()) {
                Object expectedValue = expectedJsonObject.get(expectedKey);
                Object actualValue = actualJsonObject.get(expectedKey);
                assertEquals(expectedValue, actualValue);
            }
        }
    }

    private File[] getTestPropertyFiles() {
        return getTestFilesByExtension("properties");
    }
    private File[] getTestJsonFiles() {
        return getTestFilesByExtension("json");
    }


    private File[] getTestFilesByExtension(String extension) {
        File dataFolder = getFile(TEST_BUNDLE_FOLDER);
        File[] propertyFiles = dataFolder.listFiles((file, name) -> name.startsWith(TEST_BUNDLE_NAME) &&
                name.endsWith("." + extension));
        if(propertyFiles == null || propertyFiles.length == 0) {
            throw new IllegalStateException("No test files found matching '" + TEST_BUNDLE_FOLDER + "/" +
                    TEST_BUNDLE_NAME + "**." + extension);
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
