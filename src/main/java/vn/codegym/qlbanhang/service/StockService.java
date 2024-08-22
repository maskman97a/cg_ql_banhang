package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.*;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.entity.ProductEntity;
import vn.codegym.qlbanhang.entity.StockEntity;
import vn.codegym.qlbanhang.enums.ErrorType;
import vn.codegym.qlbanhang.enums.StockTransType;
import vn.codegym.qlbanhang.exception.BusinessException;
import vn.codegym.qlbanhang.model.CategoryModel;
import vn.codegym.qlbanhang.model.ProductModel;
import vn.codegym.qlbanhang.model.StockModel;
import vn.codegym.qlbanhang.utils.DataUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class StockService extends BaseService {
    private static final StockService inst = new StockService();
    private final StockModel stockModel;
    private final ProductModel productModel;
    private final CategoryModel categoryModel;

    public static StockService getInstance() {
        return inst;
    }

    private StockService() {
        super(StockModel.getInstance());
        this.stockModel = (StockModel) super.baseModel;
        this.productModel = ProductModel.getInstance();
        this.categoryModel = CategoryModel.getInstance();
    }

    public BaseResponse<List<StockDto>> searchStock(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        BaseResponse<List<StockDto>> baseResponse = new BaseResponse<>();
        List<CategoryDto> categoryDtoList = categoryModel.getLstCategory();
        req.setAttribute("lstCategory", categoryDtoList);
        String keyword = req.getParameter("keyword");
        int size = 5;
        if (req.getParameter("size") != null) {
            size = Integer.parseInt(req.getParameter("size"));
        }
        int page = 1;
        if (req.getParameter("page") != null) {
            page = Integer.parseInt(req.getParameter("page"));
            if (page == 0) page = 1;
        }
        req.setAttribute("currentPage", page);
        List<StockDto> stockList = stockModel.searchListStock(keyword, size, page);
        int count = stockModel.countListStock(keyword);
        getPaging(req, resp, count, size, page);
        req.setAttribute("renderStockAdmin", true);
        req.setAttribute("renderStock", true);
        req.setAttribute("stockList", stockList);
        baseResponse.setAdditionalData(stockList);
        req.getRequestDispatcher("/views/admin/admin.jsp").forward(req, resp);
        return baseResponse;
    }

    public BaseResponse<StockDto> addProductToStock(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException, SQLException {
        BaseResponse<StockDto> baseResponse = new BaseResponse<>();
        try {
            String productIdStr = req.getParameter("productId");
            if (productIdStr == null) {
                throw new BusinessException(ErrorType.INVALID_DATA.getErrorCode(), "Thông tin sản phẩm không hợp lệ");
            }
            int productId = Integer.parseInt(productIdStr);
            ProductEntity productEntity = productModel.findProductById(productId);
            if (DataUtil.isNullObject(productEntity)) {
                throw new BusinessException(ErrorType.INVALID_DATA.getErrorCode(), "Thông tin sản phẩm không hợp lệ");
            }
            String quantityStr = req.getParameter("quantity");
            if (quantityStr == null) {
                throw new BusinessException(ErrorType.INVALID_DATA.getErrorCode(), "Số lượng không hợp lệ");
            }
            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                throw new BusinessException(ErrorType.INVALID_DATA.getErrorCode(), "Số lượng không hợp lệ");
            }
            BaseSearchDto baseSearchStockDto = new BaseSearchDto();
            baseSearchStockDto.getQueryConditionDtos().add(QueryConditionDto.newAndCondition("product_id", "=", productId));
            StockEntity stockEntity = (StockEntity) stockModel.findOne(baseSearchStockDto);
            if (DataUtil.isNullObject(stockEntity)) {
                stockEntity = new StockEntity();
                stockEntity.setProductId(productId);
                stockEntity.setAvailableQuantity(quantity);
                stockEntity.setTotalQuantity(quantity);
                stockEntity.setPendingQuantity(0);
                stockEntity.setStatus(Const.STATUS_ACTIVE);
            } else {
                stockEntity.setAvailableQuantity(stockEntity.getAvailableQuantity() + quantity);
                stockEntity.setTotalQuantity(stockEntity.getTotalQuantity() + quantity);
            }
            stockEntity = (StockEntity) stockModel.save(stockEntity);
            baseResponse.setAdditionalData(modelMapper.map(stockEntity, StockDto.class));
        } catch (BusinessException ex) {
            baseResponse.setError(ex);
        } catch (Exception ex) {
            baseResponse.setError(ErrorType.INTERNAL_SERVER_ERROR);
        }
        resp.setContentType("application/json; charset=UTF-8");
        resp.getWriter().write(gson.toJson(baseResponse));
        return baseResponse;
    }

    public BaseResponse<StockDto> editStock(HttpServletRequest req, HttpServletResponse resp) {
        BaseResponse baseResponse = new BaseResponse<>();
        try {
            String stockIdStr = req.getParameter("stockId");
            String newAvailableQuantity = req.getParameter("stockId");
            String newTotalQuantity = req.getParameter("newTotalQuantity");

        } catch (Exception ex) {
            baseResponse.setError(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return baseResponse;
    }

    public BaseResponse executeStock(ExecuteStockDto executeStockDto) {
        BaseResponse baseResponse = new BaseResponse();
        try {
            switch (executeStockDto.getOrderStatus()) {
                case Const.OrderStatus.COMPLETED:
                    //Đơn hàng đã vận chuyển và giao cho khách thành công
                    for (UpdateStockDto updateStockDto : executeStockDto.getUpdateStockList()) {
                        StockEntity stockEntity = stockModel.getValidStock(updateStockDto.getProductId(), updateStockDto.getQuantity());
                        if (!DataUtil.isNullObject(stockEntity)) {
                            stockModel.updateStock(stockEntity, updateStockDto.getQuantity(), StockTransType.EXPORT.name());
                        } else {
                            throw new BusinessException(ErrorType.INVALID_DATA.getErrorCode(), "Thông tin kho không hợp lệ hoặc không còn đủ số lượng");
                        }
                    }
                    break;
                case Const.OrderStatus.ACCEPTED:
                    //Đơn hàng đã được xác nhận và chờ giao hàng
                    for (UpdateStockDto updateStockDto : executeStockDto.getUpdateStockList()) {
                        StockEntity stockEntity = stockModel.getValidStock(updateStockDto.getProductId(), updateStockDto.getQuantity());
                        if (!DataUtil.isNullObject(stockEntity)) {
                            stockModel.updateStock(stockEntity, updateStockDto.getQuantity(), StockTransType.PENDING.name());
                        } else {
                            throw new BusinessException(ErrorType.INVALID_DATA.getErrorCode(), "Thông tin kho không hợp lệ hoặc không còn đủ số lượng");
                        }
                    }
                    break;
                case Const.OrderStatus.REFUND:
                    //Đơn hàng được yêu cầu hoàn trả
                    for (UpdateStockDto updateStockDto : executeStockDto.getUpdateStockList()) {
                        StockEntity stockEntity = stockModel.getValidStock(updateStockDto.getProductId(), updateStockDto.getQuantity());
                        if (!DataUtil.isNullObject(stockEntity)) {
                            stockModel.updateStock(stockEntity, updateStockDto.getQuantity(), StockTransType.IMPORT.name());
                        } else {
                            throw new BusinessException(ErrorType.INVALID_DATA.getErrorCode(), "Thông tin kho không hợp lệ hoặc không còn đủ số lượng");
                        }
                    }
                    break;
            }
        } catch (BusinessException ex) {
            baseResponse.setError(ex);
        } catch (Exception ex) {
            baseResponse.setError(ErrorType.INTERNAL_SERVER_ERROR);
        }
        return baseResponse;
    }
}
