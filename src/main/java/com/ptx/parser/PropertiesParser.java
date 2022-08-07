package com.ptx.parser;

import com.ptx.dto.PropertiesBundle;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.properties.SortedProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

@Log4j2
public class PropertiesParser {


    public PropertiesBundle parseProperties(String bundleName, File[] propertyFiles) {
        //key1=property name, key2=file name, value=cell text
        Map<String, Map<String, String>> propertiesAndValues = new LinkedHashMap<>();


        //parse all properties file and populate our Map
        List<String> fileAliases = new ArrayList<>();
        for (File propertyFile : propertyFiles) {
            String fileAlias = propertyFile.getName().replace(bundleName, "")
                    .replace(".properties", "");
            if (fileAlias.startsWith("_")) {
                fileAlias = fileAlias.substring(1);
            }

            fileAliases.add(fileAlias);
            parsePropertyFile(propertiesAndValues, propertyFile, fileAlias);
        }


        return new PropertiesBundle(bundleName, fileAliases.toArray(new String[0]), propertiesAndValues);

    }

    private void parsePropertyFile(Map<String, Map<String, String>> propertiesAndValues, File propertyFile, String fileAlias) {
        log.debug("Parsing file {}...", propertyFile.getName());

        try {
            SortedProperties prop = new SortedProperties();
            prop.load(new FileInputStream(propertyFile));

            Iterator<Object> propIterator = prop.keys().asIterator();
            while (propIterator.hasNext()) {
                Object key = propIterator.next();
                Object value = prop.get(key);

                propertiesAndValues
                        .computeIfAbsent(String.valueOf(key), key2 -> new LinkedHashMap<>())
                        .put(fileAlias, String.valueOf(value));
            }
        } catch (IOException io) {
            throw new IllegalStateException("Unable to parse properties due to " + io.getClass().getSimpleName(), io);
        }

        log.debug("Finished parsing file {}", propertyFile.getName());
    }

}
