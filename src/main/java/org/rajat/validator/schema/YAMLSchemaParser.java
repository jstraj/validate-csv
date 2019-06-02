package org.rajat.validator.schema;

import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;

public class YAMLSchemaParser implements CsvSchemaParser {

    private Yaml yaml;

    public YAMLSchemaParser() {
        this.yaml = new Yaml();
    }

    @Override
    public CsvSchema parse(String data) {
        CsvSchema csvSchema = yaml.loadAs(data, CsvSchema.class);
        return csvSchema;
    }

    @Override
    public CsvSchema parse(File file) throws IOException {
        return parse(FileUtils.readFileToString(file, "utf-8"));
    }
}
