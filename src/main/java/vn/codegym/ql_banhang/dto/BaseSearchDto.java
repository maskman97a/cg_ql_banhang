package vn.codegym.ql_banhang.dto;

import java.util.ArrayList;
import java.util.List;

public class BaseSearchDto {
    private String keyword;
    private int page;
    private int size;
    private List<Condition> conditions;
    private List<JoinCondition> joinConditions;

    public BaseSearchDto() {
        this.conditions = new ArrayList<>();
        this.joinConditions = new ArrayList<>();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<JoinCondition> getJoinConditions() {
        return joinConditions;
    }

    public void setJoinConditions(List<JoinCondition> joinConditions) {
        this.joinConditions = joinConditions;
    }
}
