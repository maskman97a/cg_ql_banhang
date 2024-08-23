package vn.codegym.qlbanhang.service;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import vn.codegym.qlbanhang.dto.Cart;
import vn.codegym.qlbanhang.dto.CartProductDto;
import vn.codegym.qlbanhang.dto.PagingDto;
import vn.codegym.qlbanhang.entity.BaseData;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.model.BaseModel;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class BaseService {
    protected final Logger log = Logger.getLogger("System Log");
    protected final BaseModel baseModel;
    protected final ModelMapper modelMapper;
    protected final Gson gson;

    public BaseService(BaseModel baseModel) {
        this.baseModel = baseModel;
        this.modelMapper = new ModelMapper();
        this.gson = new Gson();
    }

    protected PagingDto getPaging(HttpServletRequest req, HttpServletResponse resp, int count, int size, int page) {
        PagingDto pagingDto = new PagingDto();
        req.setAttribute("totalRow", count);
        pagingDto.setTotalRow(count);
        req.setAttribute("currentPage", page);
        pagingDto.setCurrentPage(page);
        BigDecimal bCount = new BigDecimal(count);
        BigDecimal bSize = new BigDecimal(size);
        // Thực hiện phép chia và làm tròn lên
        BigDecimal totalPage = bCount.divide(bSize, 0, RoundingMode.CEILING);
        req.setAttribute("totalPage", totalPage);
        pagingDto.setTotalPage(totalPage.intValue());

        int tabSize = 10;
        BigDecimal bTabSize = new BigDecimal(tabSize);
        BigDecimal countTab = totalPage.divide(bTabSize, 0, RoundingMode.CEILING);
        for (int tabIndex = 0; tabIndex < countTab.intValue(); tabIndex++) {
            int startValue = tabIndex * tabSize + 1;
            int endValue = (tabIndex + 1) * tabSize;
            if (page >= startValue && page <= endValue) {
                if (tabIndex == 0) {
                    req.setAttribute("firstTab", true);
                    pagingDto.setFirstTab(true);
                }
                if (tabIndex == countTab.intValue() - 1) {
                    req.setAttribute("lastTab", true);
                    pagingDto.setLastTab(true);
                }

                if (tabIndex == 0) {
                    startValue = 1;
                }
                if (tabIndex == countTab.intValue() - 1) {
                    endValue = startValue + totalPage.intValue() % tabSize - 1;
                }
                req.setAttribute("beginPage", startValue);
                pagingDto.setBeginPage(startValue);
                req.setAttribute("endPage", endValue);
                pagingDto.setEndPage(endValue);
                break;
            }
        }
        return pagingDto;
    }

    public BaseData findAll() {
        try {
            List<BaseEntity> lstData = baseModel.findAll();
            return new BaseData(lstData.size(), lstData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new BaseData(0, new ArrayList<>());
    }

    public BaseEntity findById(Integer id) {
        try {
            return baseModel.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    protected void renderPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Object cartProductJson = session.getAttribute("cartProductJson");
        Cart cart;
        if (DataUtil.isNullObject(cartProductJson)) {
            cart = new Cart(new ArrayList<>());
        } else {
            cart = gson.fromJson((String) cartProductJson, Cart.class);
        }
        List<CartProductDto> cartProductDtoList = cart.getCartProductList();

        req.setAttribute("cartCount", cartProductDtoList.size());
        if (DataUtil.safeEqual(req.getAttribute("renderAdmin"), "true")) {
            req.getRequestDispatcher(req.getContextPath() + "/views/admin/admin.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher(req.getContextPath() + "/views/home/home.jsp").forward(req, resp);
        }
    }
}
