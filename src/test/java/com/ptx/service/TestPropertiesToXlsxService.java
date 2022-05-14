package com.ptx.service;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.ResourceUtils;

import java.io.*;


@SpringBootTest
@Import(PropertiesToXlsxService.class)
public class TestPropertiesToXlsxService {


    public static final String TEST_BUNDLE_FOLDER = "classpath:/data";
    public static final String TEST_BUNDLE_NAME = "messages";
    public static final String TEST_OUTPUT_FILE = "out/test-results/messages.xlsx";


    @Autowired
    private PropertiesToXlsxService propertiesToXlsxService;

    @Test
    public void generateXlsx() throws IOException {

        File dataFolder = ResourceUtils.getFile(TEST_BUNDLE_FOLDER);
        File[] propertyFiles = dataFolder.listFiles((file, name) -> name.startsWith(TEST_BUNDLE_NAME) && name.endsWith(".properties"));

        byte[] xlsxFile = propertiesToXlsxService.generateXlsx(propertyFiles);

        //Write file to disk
        try(FileOutputStream os = FileUtils.openOutputStream(new File(TEST_OUTPUT_FILE))) {
            os.write(xlsxFile);
        }

        //TODO: Assertions

    }



}
