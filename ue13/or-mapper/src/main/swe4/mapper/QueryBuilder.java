package swe4.mapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class QueryBuilder {

    private Class<?> entityType;

    public FieldDescription getKeyDescription(List<FieldDescription> fieldDescriptions) {
        List<FieldDescription> keys = new ArrayList<>();
        for (var f : fieldDescriptions) {
            if (f.isKey()) {
                keys.add(f);
            }
        }

        if (keys.isEmpty()) {
            throw new DataAccessException("Entity %s has no key column!".formatted(entityType.getSimpleName()));
        } else if (keys.size() > 1) {
            throw new DataAccessException("Entity %s has multiple key columns (not supported)!".formatted(entityType.getSimpleName()));
        }

        return keys.getFirst();
    }

    public QueryBuilder(Class<?> entityType) { // <?> ... "unbounded wildcard"
        this.entityType = entityType;
    }

    public String getTableName() {
        Table[] tableAnnotations = entityType.getDeclaredAnnotationsByType(Table.class);// declared bedeutet "nicht vererbt" (eigene Felder)
        if (tableAnnotations.length == 0)
            throw new DataAccessException("Entity %s not mapped (mapping annotation is missing)!".formatted(entityType.getName()));

        if (tableAnnotations[0].name().isEmpty()) // empty = default value lt. Annotation
            return entityType.getSimpleName();

        return tableAnnotations[0].name();
    }

    public List<FieldDescription> getFieldDescriptions() {
        List<FieldDescription> fieldDescriptions = new ArrayList<>();
        for (Field field : entityType.getDeclaredFields()) {
            Column[] columnAnnotations = field.getDeclaredAnnotationsByType(Column.class);
            if (columnAnnotations.length > 0) {
                String columName = field.getName();
                if (!columnAnnotations[0].name().isEmpty()) {
                    columName = columnAnnotations[0].name();
                }

                fieldDescriptions.add(new FieldDescription(field.getName(), columName, columnAnnotations[0].isKey()));
            }
        }

        return fieldDescriptions;
    }

    public String buildInsertQuery() {
        var fieldDescriptions = getFieldDescriptions();
        StringJoiner columnString = new StringJoiner(", ");
        for (var f : fieldDescriptions) {
            columnString.add(f.getColumnName());
        }

        StringJoiner questionMarks = new StringJoiner(", ");
        for (var f : fieldDescriptions) {
            questionMarks.add("?");
        }

        return "insert into %s (%s) values (%s)".formatted(getTableName(), columnString, questionMarks);
    }

    public String buildSelectByIdQuery() {
        var fieldDescriptions = getFieldDescriptions();

        StringJoiner columnString = new StringJoiner(", ");
        for (var f : fieldDescriptions) {
            columnString.add(f.getColumnName());
        }

        return "select %s from %s where %s = ?".formatted(columnString, getTableName(),
                getKeyDescription(fieldDescriptions).getColumnName());
    }
}
