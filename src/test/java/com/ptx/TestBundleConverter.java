package com.ptx;

import bad.robot.excel.matchers.WorkbookMatcher;
import com.ptx.BundleConverter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;


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

        bundleConverter.convertPropertiesToXlsx(TEST_BUNDLE_NAME, propertyFiles, outputFolder);

        File createdFile = getFile(outputFolder, TEST_BUNDLE_NAME + ".xlsx");
        assertTrue(createdFile.exists());

        XSSFWorkbook outcome = new XSSFWorkbook(createdFile);

        File expectedFile = getFile(EXPECTED_RESULT);
        XSSFWorkbook expectedWorkbook = new XSSFWorkbook(expectedFile);

        Matcher<Workbook> workbookMatcher = WorkbookMatcher.sameWorkbook(expectedWorkbook);
        boolean matches = workbookMatcher.matches(outcome);
        assertTrue(matches);
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

    private void saveFileToDisk(byte[] xlsxFile) throws IOException {
        File outputFolder = new File(TEST_OUTPUT_FOLDER);
        if(!outputFolder.exists()) {
            if (!outputFolder.mkdirs()) {
                throw new IllegalStateException("Unable to create output folder");
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(TEST_OUTPUT_FOLDER + "generateXlsx.xlsx")) {
            outputStream.write(xlsxFile);
        }
    }

    private File getFile(String testBundleFolder) {
        URL fileUrl = Objects.requireNonNull(getClass().getClassLoader().getResource(testBundleFolder));
        return new File(fileUrl.getFile());
    }


    private File getFile(File folder, String fileName) throws IOException {
        File[] files = folder.listFiles((dir, name) -> name.equals(fileName));
        if(files == null && files.length == 0) {
            throw new IOException("No file called '" + fileName + "' found in " + folder.getPath());
        }
        return files[0];
    }

}
