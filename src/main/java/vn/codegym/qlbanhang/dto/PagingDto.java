package vn.codegym.qlbanhang.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagingDto {
    private int totalRow;
    private int currentPage;
    private int totalPage;
    private boolean firstTab;
    private boolean lastTab;
    private int beginPage;
    private int endPage;
}
