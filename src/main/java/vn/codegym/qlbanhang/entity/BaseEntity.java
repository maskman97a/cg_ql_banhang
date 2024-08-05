package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Map;

@Getter
@Setter
public abstract class BaseEntity {
    protected int id;
    protected int status;
    protected String createdBy;
    protected Date createdDate;
    protected String updatedBy;
    protected Date updatedDate;

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
