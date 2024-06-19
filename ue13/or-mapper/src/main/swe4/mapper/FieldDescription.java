package swe4.mapper;

public class FieldDescription {
    private String fieldName;
    private String columnName;
    private boolean isKey;

    public FieldDescription(String fieldName, String columnName, boolean isKey) {
        this.fieldName = fieldName;
        this.columnName = columnName;
        this.isKey = isKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getColumnName() {
        return columnName;
    }

    public boolean isKey() {
        return isKey;
    }
}
