package org.rajat.validator.validate;

import org.rajat.validator.schema.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface CsvValidator {

    public List<ErrorMessage> validate(File csvFile, CsvSchema schema) throws IOException;

}
