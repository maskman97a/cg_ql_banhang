package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.QueryConditionDto;
import vn.codegym.qlbanhang.dto.OrdersDto;
import vn.codegym.qlbanhang.entity.OrderEntity;
import vn.codegym.qlbanhang.enums.OrderStatus;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderModel extends BaseModel {
    private static final OrderModel inst = new OrderModel();

    private OrderModel() {
        super(OrderEntity.class);
    }

    public static OrderModel getInstance() {
        return inst;
    }

    public OrderEntity getByCode(String code) throws SQLException {
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        QueryConditionDto queryConditionDto = QueryConditionDto.newAndCondition("code", "=", code);
        baseSearchDto.setQueryConditionDtos(Collections.singletonList(queryConditionDto));
        return (OrderEntity) findOne(baseSearchDto);
    }


    public List<OrdersDto> findOrderByKeyword(BaseSearchDto baseSearchDto) {
        List<OrdersDto> lstResult = new ArrayList<>();
        try {
            String sql = this.getSearchSQL(baseSearchDto);
            sql += " order by o.order_date desc ";
            sql += " limit ? offset ?";
            PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
            int index = 1;
            if (baseSearchDto != null) {
                if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                    preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                }
                if (baseSearchDto.getStatus() != null) {
                    preparedStatement.setInt(index++, baseSearchDto.getStatus());
                }
            }
            preparedStatement.setInt(index++, baseSearchDto.getSize());
            preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                OrdersDto dto = new OrdersDto();
                dto.setId(rs.getInt("id"));
                dto.setCode(rs.getString("code"));
                dto.setCustomerId(rs.getInt("customer_id"));
                dto.setCustomerName(rs.getString("customerName"));
                dto.setEmail(rs.getString("email"));
                dto.setPhone(rs.getString("phone"));
                dto.setAddress(rs.getString("address"));
                dto.setOrderDateStr(rs.getString("order_date_str"));
                dto.setStatus(rs.getInt("status"));
                dto.setStatusName(OrderStatus.getDescription(dto.getStatus()));
                lstResult.add(dto);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lstResult;
    }

    public Integer countOrder(BaseSearchDto baseSearchDto) throws SQLException {
        String sql = " SELECT count(1) from (" + getSearchSQL(baseSearchDto) + ") as countLst ";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        int index = 1;
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
            }
            if (baseSearchDto.getStatus() != null) {
                preparedStatement.setInt(index++, baseSearchDto.getStatus());
            }
        }
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }


    public String getSearchSQL(BaseSearchDto baseSearchDto) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select o.id,o.code,o.customer_id,c.name as customerName,c.email,c.phone,o.address,date_format(o.order_date, '%d/%m/%Y') as order_date_str, ");
        sb.append(" o.status ");
        sb.append("  FROM orders o ");
        sb.append("       left join customer c on c.id = o.customer_id and c.status = 1 ");
        sb.append(" WHERE 1= 1 ");
        if (!DataUtil.isNullObject(baseSearchDto)) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                sb.append(" AND ( o.code LIKE ? OR c.phone LIKE ? ) ");
            }
            if (baseSearchDto.getStatus() != null) {
                sb.append(" AND o.status = ?");
            }
        }
        return sb.toString();
    }


    public int updateOrder(OrderEntity orderEntity, String action) throws SQLException {
        StringBuilder sb = new StringBuilder("");
        sb.append("UPDATE orders " +
                "   SET updated_date = CURRENT_TIMESTAMP , " +
                "       updated_by = ? ");
        if (!DataUtil.isNullObject(orderEntity)) {
            switch (action) {
                case "confirm":
                    sb.append(" ,status = 3 ");
                    break;
                case "complete":
                    sb.append(" ,status = 1 ");
                    break;
                case "cancel":
                    sb.append(" ,status = 2 ");
                    break;
            }
        }
        sb.append(" WHERE id = ? ");
        switch (action) {
            case "confirm":
            case "cancel":
                sb.append(" AND status = 0 ");
                break;
            case "complete":
                sb.append(" AND status = 3 ");
                break;
        }
        PreparedStatement preparedStatement = getConnection().prepareStatement(sb.toString());
        int index = 1;
        preparedStatement.setString(index++, orderEntity.getUpdatedBy());
        preparedStatement.setInt(index++, orderEntity.getId());
        return preparedStatement.executeUpdate();
    }
}
