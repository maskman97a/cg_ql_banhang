package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteType extends BaseEntity {
    private static final String TABLE_NAME = "note_type";
    private static final String SEARCH_COLUMN = "name";
    private int index;
    private int id;
    private String name;
    private String description;

    public NoteType() {
    }

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSearchColumn() {
        return SEARCH_COLUMN;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
