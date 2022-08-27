package com.ptx;

import com.ptx.dto.PropertiesBundle;
import com.ptx.generator.PropertiesGenerator;
import com.ptx.generator.XlsxGenerator;
import com.ptx.parser.PropertiesParser;
import com.ptx.parser.XlsxParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BundleConverter {


    public void convertPropertiesToXlsx(File propertyFilesLocation, String bundleName, File outputFolder) throws IOException {
        PropertiesBundle propertiesBundle = new PropertiesParser().parseProperties(propertyFilesLocation, bundleName);
        byte[] xlsxBytes = new XlsxGenerator().generateXlsx(propertiesBundle);

        saveXlsxToDisk(outputFolder, bundleName, xlsxBytes);
    }

    public void convertXlsxToProperties(InputStream xlsx, File outputFolder) {
        PropertiesBundle propertiesBundle = new XlsxParser().parse(xlsx);
        new PropertiesGenerator().generateProperties(propertiesBundle, outputFolder);
    }




    private void saveXlsxToDisk(File outputFolder, String fileName, byte[] xlsxFile) throws IOException {
        if(!outputFolder.exists()) {
            if (!outputFolder.mkdirs()) {
                throw new IllegalStateException("Unable to create output folder");
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(new File(outputFolder, fileName + ".xlsx"))) {
            outputStream.write(xlsxFile);
        }
    }

}
