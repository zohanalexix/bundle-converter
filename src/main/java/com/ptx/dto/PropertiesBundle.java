package com.ptx.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PropertiesBundle {

    private final String bundleName;
    private final String[] valueAliases;
    private final Map<String, Map<String, String>> values;


}
