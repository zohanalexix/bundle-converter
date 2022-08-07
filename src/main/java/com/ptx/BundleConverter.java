package com.ptx;

import com.ptx.dto.PropertiesBundle;
import com.ptx.generator.XlsxGenerator;
import com.ptx.parser.PropertiesParser;

import java.io.File;

public class BundleConverter {




    public byte[] convertPropertiesToXlsx(String bundleName, File[] propertyFiles) {
        PropertiesBundle propertiesBundle = new PropertiesParser().parseProperties(bundleName, propertyFiles);
        return new XlsxGenerator().generateXlsx(propertiesBundle);
    }


}
