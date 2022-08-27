package org.alx.bc.parser;

import lombok.extern.slf4j.Slf4j;
import org.alx.bc.dto.PropertiesBundle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JsonParser {

    public PropertiesBundle parseProperties(File propertiesFileLocation, String bundleName) {

        File[] propertyFiles = propertiesFileLocation.listFiles(
                (file, name) -> name.startsWith(bundleName) && name.endsWith(".json")
        );
        if(propertyFiles == null || propertyFiles.length == 0) {
            throw new IllegalArgumentException("No property file found at location " + propertiesFileLocation);
        }

        //key1=property name, key2=file name, value=cell text
        Map<String, Map<String, String>> propertiesAndValues = new LinkedHashMap<>();


        //parse all properties file and populate our Map
        List<String> fileAliases = new ArrayList<>();
        for (File propertyFile : propertyFiles) {
            String fileAlias = propertyFile.getName().replace(bundleName, "")
                    .replace(".json", "");
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
            String json = Files.readString(propertyFile.toPath());
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(json);

            Set<?> set = jsonObject.keySet();
            List<Object> sortedKeySet = set.stream().sorted().collect(Collectors.toList());

            for (Object key : sortedKeySet) {

                Object value = jsonObject.get(key);

                propertiesAndValues
                        .computeIfAbsent(String.valueOf(key), key2 -> new LinkedHashMap<>())
                        .put(fileAlias, String.valueOf(value));

            }
        } catch (IOException | ParseException io) {
            throw new IllegalStateException("Unable to parse properties due to " + io.getClass().getSimpleName(), io);
        }

        log.debug("Finished parsing file {}", propertyFile.getName());
    }

}
