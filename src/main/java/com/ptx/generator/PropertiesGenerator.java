package com.ptx.generator;

import com.ptx.dto.PropertiesBundle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class PropertiesGenerator {

    public void generateProperties(PropertiesBundle propertiesBundle, File outputFolder) {

        if(outputFolder.exists() && !outputFolder.isDirectory()) {
            throw new IllegalArgumentException("outputPath is not a folder");
        }
        if(!outputFolder.exists() && !outputFolder.mkdirs()) {
            throw new IllegalArgumentException("Unable to create the directory " + outputFolder.getPath());
        }


        String bundleName = propertiesBundle.bundleName();
        Map<String, Map<String, String>> values = propertiesBundle.values();

        for (String valueAlias : propertiesBundle.valueAliases()) {


            File propertyFile = new File(outputFolder, bundleName + "_" + valueAlias + ".properties");
            try (PrintWriter pw = new PrintWriter(new FileWriter(propertyFile))) {

                for (Map.Entry<String, Map<String, String>> valuesEntryMap : values.entrySet()) {
                    String propertyName = valuesEntryMap.getKey();
                    Map<String, String> propertyValues = valuesEntryMap.getValue();

                    pw.println(propertyName + " = " + propertyValues.get(valueAlias));
                }
            } catch (IOException e) {
                throw new IllegalStateException("Unable to write file " + propertyFile.getPath(), e);
            }
        }
    }
}
