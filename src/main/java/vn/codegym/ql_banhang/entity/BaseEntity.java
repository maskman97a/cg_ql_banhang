package vn.codegym.ql_banhang.entity;

import java.util.Map;

public abstract class BaseEntity {
    private Map<String, Object> mapValue;

    public BaseEntity() {
    }

    public static BaseEntity getInstance(String tableName) {
        if (tableName.equals(Note.getTableName())) {
            return new Note();
        }
        if (tableName.equals(NoteType.getTableName())) {
            return new NoteType();
        }
        return null;
    }

    public Map<String, Object> getMapValue() {
        return mapValue;
    }

    public void setMapValue(Map<String, Object> mapValue) {
        this.mapValue = mapValue;
    }
}
