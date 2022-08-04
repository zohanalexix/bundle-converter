package com.ptx.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class TestPropertiesToXlsxService {


    public static final String TEST_BUNDLE_FOLDER = "data";
    public static final String TEST_BUNDLE_NAME = "messages";
    public static final String TEST_OUTPUT_FILE = "out/test-results/messages.xlsx";


    private final PropertiesToXlsxService propertiesToXlsxService = new PropertiesToXlsxService();

    @Test
    public void generateXlsx() throws IOException {

        File dataFolder = new File(getClass().getClassLoader().getResource(TEST_BUNDLE_FOLDER).getFile());
        File[] propertyFiles = dataFolder.listFiles((file, name) -> name.startsWith(TEST_BUNDLE_NAME) &&
                name.endsWith(".properties"));
        if(propertyFiles == null || propertyFiles.length == 0) {
            throw new IllegalStateException("No test files found matching '" + TEST_BUNDLE_FOLDER + "/" +
                    TEST_BUNDLE_NAME + "**.properties");
        }

        byte[] xlsxFile = propertiesToXlsxService.generateXlsx(TEST_BUNDLE_NAME, propertyFiles);

        //Write file to disk
        try(FileOutputStream os = FileUtils.openOutputStream(new File(TEST_OUTPUT_FILE))) {
            os.write(xlsxFile);
        }

        //TODO: Assertions

    }



}
