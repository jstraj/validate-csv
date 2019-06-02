package org.rajat.validator.validate;

import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.rajat.validator.schema.CsvColumn;
import org.rajat.validator.schema.CsvSchema;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleCsvValidator implements CsvValidator {

    @Override
    public List<ErrorMessage> validate(File csvFile, CsvSchema schema) throws IOException {
        return validationHelper(csvFile, schema);
    }

    public List<ErrorMessage> validationHelper(File file, CsvSchema schema) throws IOException {

        List<ErrorMessage> errorMessageList = new ArrayList<>();
        List<String> schemaFields = schema.getColumns().stream().map(col -> col.getColumnName()).collect(Collectors.toList());
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        try {
            boolean checkHeader = true;
            try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
                CsvRow row;
                while ((row = csvParser.nextRow()) != null) {
                    try {
                        //System.out.println(row);

                        // Validation 1: Check the number of columns is equal to totalColumns.
                        if (row.getFieldCount() != schema.getTotalColumns()) {
                            errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR, "The totalColumns specified in the schema(" + schema.getTotalColumns() + ") is " +
                                    "not equal to total columns in the csv(" + row.getFieldCount() + ") at row " +
                                    row.getOriginalLineNumber() + "."));
                        }

                        // Validation 2: Check all field names in schema matches the ones in the csv.
                        if (checkHeader) {              // Check header just once
                            errorMessageList.addAll(compareHeaders(csvParser.getHeader(), schemaFields));
                            checkHeader = false;
                        }

                        // Validation 3: Check each field's value matches its corresponding regex or not.
                        for (int i = 0; i < schema.getTotalColumns(); i++) {
                            CsvColumn column = schema.getColumns().get(i);
                            String fieldValue = row.getField(column.getColumnName());
                            //System.out.println(column);

                            if (fieldValue == null) {
                                errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.WARN, "The field " + column +
                                        " value(" + fieldValue + ") is null at row " +
                                        row.getOriginalLineNumber() + "."));
                            } else if (fieldValue.equalsIgnoreCase("")) {
                                errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.WARN, "The field " + column +
                                        " value(" + fieldValue + ") is empty at row " +
                                        row.getOriginalLineNumber() + "."));
                            } else {
                                if (!column.getCompiledRegexPattern().matcher(fieldValue).matches()) {
                                    errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR, "The field " + column +
                                            " value(" + fieldValue + ") doesn't matches its regex at row " +
                                            row.getOriginalLineNumber() + "."));
                                }
                            }
                        }
                    } catch (Exception ex) {
                        StringWriter sw = new StringWriter();
                        ex.printStackTrace(new PrintWriter(sw));
                        errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR,
                                "Exception Occurred while reading row " + row.getOriginalLineNumber() + " : " + sw.toString()));
                    }
                }
            }
        } catch (Exception ex) {
            StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR,
                    "Exception Occurred while reading file: " + sw.toString()));
        }

        return errorMessageList;
    }

    private List<ErrorMessage> compareHeaders(List<String> csvHeader, List<String> schemaHeader) {

        List<ErrorMessage> errorMessageList = new ArrayList<>();

        // Validation 2a: If csvHeader and schemaHeader are not equal in size.
        if (csvHeader.size() != schemaHeader.size())
            errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR,
                    "The csvHeader and the schemaHeader are not equal in length."));

        // Validation 2b: If there's some column mismatch between csvHeader and schemaHeader.
        for (int i = 0; i < csvHeader.size(); i++) {
            if (!csvHeader.get(i).equals(schemaHeader.get(i)))
                errorMessageList.add(new ErrorMessage(ErrorMessage.ErrorMessageType.ERROR,
                        "The csvHeader(" + csvHeader.get(i) + ") is not equal to the schemaHeader(" + schemaHeader.get(i) + ")"));
        }

        return errorMessageList;
    }

}
