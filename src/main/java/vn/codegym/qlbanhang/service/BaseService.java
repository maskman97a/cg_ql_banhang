package vn.codegym.qlbanhang.service;

import org.modelmapper.ModelMapper;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.Condition;
import vn.codegym.qlbanhang.entity.BaseData;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.model.BaseModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class BaseService {
    protected final BaseModel baseModel;
    protected final ModelMapper modelMapper;

    public BaseService(BaseModel baseModel) {
        this.baseModel = baseModel;
        this.modelMapper = new ModelMapper();
    }

    protected BaseData doSearch(HttpServletRequest req, HttpServletResponse resp, BaseSearchDto baseSearchDto, String columnName) {
        try {
            String keyword = req.getParameter("keyword");
            int size = 10;
            if (req.getParameter("size") != null) {
                size = Integer.parseInt(req.getParameter("size"));
            }
            int page = 1;
            if (req.getParameter("page") != null) {
                page = Integer.parseInt(req.getParameter("page"));
            }
            req.setAttribute("currentPage", page);
            baseSearchDto.setKeyword(keyword);
            baseSearchDto.setSize(size);
            baseSearchDto.setPage(page);
            if (keyword != null && !keyword.isEmpty()) {
                Condition condition = new Condition();
                condition.setColumnName(columnName);
                condition.setOperator("LIKE");
                condition.setValue("%" + keyword + "%");
                baseSearchDto.getConditions().add(condition);
            }
            List<BaseEntity> lstData = baseModel.search(baseSearchDto);
            int count = baseModel.count(baseSearchDto);
            getPaging(req, resp, count, size, page);
            req.setAttribute("keyword", keyword);
            return new BaseData(count, lstData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    protected void getPaging(HttpServletRequest req, HttpServletResponse resp, int count, int size, int page) {
        req.setAttribute("totalRow", count);
        BigDecimal bCount = new BigDecimal(count);
        BigDecimal bSize = new BigDecimal(size);
        // Thực hiện phép chia và làm tròn lên
        BigDecimal totalPage = bCount.divide(bSize, 0, RoundingMode.CEILING);
        req.setAttribute("totalPage", totalPage);

        int tabSize = 10;
        BigDecimal bTabSize = new BigDecimal(10);
        BigDecimal countTab = totalPage.divide(bTabSize, 0, RoundingMode.CEILING);
        for (int tabIndex = 0; tabIndex < countTab.intValue(); tabIndex++) {
            int startValue = tabIndex * tabSize + 1;
            int endValue = (tabIndex + 1) * tabSize;
            if (page >= startValue && page <= endValue) {
                if (tabIndex == 0) {
                    req.setAttribute("firstTab", true);
                }
                if (tabIndex == countTab.intValue() - 1) {
                    req.setAttribute("lastTab", true);
                }

                if (tabIndex == 0) {
                    startValue = 1;
                }
                if (tabIndex == countTab.intValue() - 1) {
                    endValue = startValue + totalPage.intValue() % tabSize - 1;
                }
                req.setAttribute("beginPage", startValue);
                req.setAttribute("endPage", endValue);
                break;
            }
        }
    }

    public BaseData findAll() {
        try {
            List<BaseEntity> lstData = baseModel.findAll();
            return new BaseData(lstData.size(), lstData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public BaseEntity findById(Integer id) {
        try {
            return baseModel.findById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int save(BaseEntity entity) {
        return baseModel.save(entity);
    }
}
