package vn.codegym.ql_banhang.entity;

public class Note extends BaseEntity {
    private static final String TABLE_NAME = "note";
    private static final String SEARCH_COLUMN = "title";
    private int index;
    private int id;
    private String title;
    private String content;
    private int typeId;
    private NoteType noteType;

    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getSearchColumn() {
        return SEARCH_COLUMN;
    }

    public Note() {

    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }

    public Note(String title) {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
