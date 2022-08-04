package com.ptx.dto;

import lombok.Data;

import java.util.Map;

@Data
public class PropertyWithValues {

    private String propertyName;
    private Map<String, String> values;

}
