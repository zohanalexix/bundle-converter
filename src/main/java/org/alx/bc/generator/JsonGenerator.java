package org.alx.bc.generator;

import org.alx.bc.dto.PropertiesBundle;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

public class JsonGenerator {

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

            File propertyFile = new File(outputFolder, bundleName + "_" + valueAlias + ".json");
            try (PrintWriter pw = new PrintWriter(new FileWriter(propertyFile))) {

                Iterator<Map.Entry<String, Map<String, String>>> entries = values.entrySet().iterator();

                pw.println("{");
                while(entries.hasNext()) {
                    Map.Entry<String, Map<String, String>> entry = entries.next();

                    String propertyName = entry.getKey();
                    Map<String, String> propertyValues = entry.getValue();

                    pw.println("\"" + propertyName + "\": \"" + propertyValues.get(valueAlias) + "\"" + (entries.hasNext() ? "," : ""));
                }
                pw.println("}");
            } catch (IOException e) {
                throw new IllegalStateException("Unable to write file " + propertyFile.getPath(), e);
            }
        }


    }
}
