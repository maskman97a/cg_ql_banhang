package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.ExecuteStockDto;
import vn.codegym.qlbanhang.dto.UpdateStockDto;
import vn.codegym.qlbanhang.dto.response.BaseResponse;
import vn.codegym.qlbanhang.entity.StockEntity;
import vn.codegym.qlbanhang.enums.ErrorType;
import vn.codegym.qlbanhang.enums.StockTransType;
import vn.codegym.qlbanhang.exception.BusinessException;
import vn.codegym.qlbanhang.model.StockModel;
import vn.codegym.qlbanhang.utils.DataUtil;

public class StockService extends BaseService {
    private final StockModel stockModel;

    public StockService(StockModel stockModel) {
        super(StockModel.getInstance());
        this.stockModel = (StockModel) super.baseModel;
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
