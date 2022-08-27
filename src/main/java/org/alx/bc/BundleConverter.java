package org.alx.bc;

import org.alx.bc.dto.PropertiesBundle;
import org.alx.bc.generator.PropertiesGenerator;
import org.alx.bc.generator.XlsxGenerator;
import org.alx.bc.parser.PropertiesParser;
import org.alx.bc.parser.XlsxParser;

import java.io.*;

public class BundleConverter {


    public void convertPropertiesToXlsx(File propertyFilesLocation, String bundleName, File outputFolder) throws IOException {
        PropertiesBundle propertiesBundle = new PropertiesParser().parseProperties(propertyFilesLocation, bundleName);
        byte[] xlsxBytes = new XlsxGenerator().generateXlsx(propertiesBundle);

        saveXlsxToDisk(outputFolder, bundleName, xlsxBytes);
    }

    public void convertJsonPropertiesToXlsx(File propertyFilesLocation, String bundleName, File outputFolder) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void convertXlsxToProperties(InputStream xlsx, File outputFolder) {
        PropertiesBundle propertiesBundle = new XlsxParser().parse(xlsx);
        new PropertiesGenerator().generateProperties(propertiesBundle, outputFolder);
    }

    public void convertXlsxToJsonProperties(InputStream xlsx, File outputFolder) {
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
