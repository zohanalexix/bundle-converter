package org.alx.bc.dto;

import java.util.Map;

/**
 * @param bundleName Name of bundle
 * @param valueAliases In a bundle, each property may have different values. This array stored identifier of each value type.
 * @param values key1=property name, key2=file name, value=cell text
 */
public record PropertiesBundle(
        String bundleName,
        String[] valueAliases,
        Map<String, Map<String, String>> values)
{}
