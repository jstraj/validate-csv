package org.rajat.validator.schema;

import java.io.File;
import java.io.IOException;

public interface CsvSchemaParser {

    public CsvSchema parse(String data);
    public CsvSchema parse(File file) throws IOException;

}
