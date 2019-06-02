package org.rajat.validator.schema;


import java.util.regex.Pattern;

public class CsvColumn {

    private String columnName;
    private Pattern compiledRegexPattern;
    private String regexPattern;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Pattern getCompiledRegexPattern() {
        return compiledRegexPattern;
    }

    public String getRegexPattern() {
        return regexPattern;
    }

    public void setRegexPattern(String regexPattern) {
        this.regexPattern = regexPattern;
        this.compiledRegexPattern = Pattern.compile(regexPattern);
    }

    @Override
    public String toString() {
        return "{" + "columnName: " + this.getColumnName() + ", regexPattern: " + this.getRegexPattern() + "}";
    }
}
