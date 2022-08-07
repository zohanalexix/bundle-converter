package com.ptx;

import com.ptx.dto.PropertiesBundle;
import com.ptx.generator.XlsxGenerator;
import com.ptx.parser.PropertiesParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BundleConverter {




    public void convertPropertiesToXlsx(String bundleName, File[] propertyFiles, File outputFolder) throws IOException {
        PropertiesBundle propertiesBundle = new PropertiesParser().parseProperties(bundleName, propertyFiles);
        byte[] xlsxBytes = new XlsxGenerator().generateXlsx(propertiesBundle);

        saveXlsxToDisk(outputFolder, bundleName, xlsxBytes);
    }

    public void convertXlsxToProperties(File xlsx, File outputFolder) {
        throw new UnsupportedOperationException();
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
