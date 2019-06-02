package org.rajat.validator.schema;

import java.util.List;

public class CsvSchema {

    private String schemaName;
    private long totalColumns;
    private List<CsvColumn> columns;

    public long getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(long totalColumns) {
        this.totalColumns = totalColumns;
    }

    public List<CsvColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<CsvColumn> columns) {
        this.columns = columns;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }
}
