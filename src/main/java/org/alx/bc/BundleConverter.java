package org.alx.bc;

import org.alx.bc.dto.PropertiesBundle;
import org.alx.bc.generator.PropertiesGenerator;
import org.alx.bc.generator.XlsxGenerator;
import org.alx.bc.parser.PropertiesParser;
import org.alx.bc.parser.XlsxParser;

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
