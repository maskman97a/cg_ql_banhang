package vn.codegym.qlbanhang.model;

import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.QueryConditionDto;
import vn.codegym.qlbanhang.dto.OrderByConditionDto;
import vn.codegym.qlbanhang.dto.ProductDto;
import vn.codegym.qlbanhang.entity.BaseData;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.ProductEntity;
import vn.codegym.qlbanhang.utils.DataUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel extends BaseModel {
    private ProductModel() {
        super(ProductEntity.class);
    }

    private static final ProductModel inst = new ProductModel();

    public static ProductModel getInstance() {
        return inst;
    }

    public ProductEntity findProductById(int id) throws SQLException {
        return (ProductEntity) findById(id);
    }


    public List<ProductEntity> findProduct() {
        try {
            List<BaseEntity> baseEntities = findAllActive();
            List<ProductEntity> productEntityList = new ArrayList<>();
            for (BaseEntity baseEntity : baseEntities) {
                productEntityList.add((ProductEntity) baseEntity);
            }
            return productEntityList;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ArrayList<>();
    }

    public BaseData findProductByKeywordAndCategoryId(String keyword, Integer categoryId, String sortCol, String sortType, Integer page, Integer size) throws SQLException {
        log.info("-----start-----");
        BaseSearchDto baseSearchDto = new BaseSearchDto();
        if (!DataUtil.isNullOrEmpty(keyword)) {
            QueryConditionDto queryConditionDto = QueryConditionDto.newAndCondition("product_name", "LIKE", "%" + keyword + "%");
            baseSearchDto.getQueryConditionDtos().add(queryConditionDto);
        }

        if (!DataUtil.isNullOrZero(categoryId)) {
            QueryConditionDto queryConditionDto = QueryConditionDto.newAndCondition("category_id", "=", categoryId);
            baseSearchDto.getQueryConditionDtos().add(queryConditionDto);
        }
        QueryConditionDto queryConditionDto = QueryConditionDto.newAndCondition("status", "=", Const.STATUS_ACTIVE);
        baseSearchDto.getQueryConditionDtos().add(queryConditionDto);

        baseSearchDto.setSize(size);
        baseSearchDto.setPage(page);
        if (DataUtil.isNullOrEmpty(sortCol)) {
            OrderByConditionDto orderByConditionDto = new OrderByConditionDto();
            orderByConditionDto.setColumnName("category_id");
            orderByConditionDto.setOrderType("ASC");
            baseSearchDto.getOrderByConditionDtos().add(orderByConditionDto);

            OrderByConditionDto orderByConditionDto2 = new OrderByConditionDto();
            orderByConditionDto2.setColumnName("product_name");
            orderByConditionDto2.setOrderType("ASC");
            baseSearchDto.getOrderByConditionDtos().add(orderByConditionDto2);
        } else {
            OrderByConditionDto orderByConditionDto = new OrderByConditionDto();
            orderByConditionDto.setColumnName(sortCol);
            if (sortType != null) {
                orderByConditionDto.setOrderType(sortType);
            }
            baseSearchDto.getOrderByConditionDtos().add(orderByConditionDto);
        }

        List<BaseEntity> baseEntities = super.search(baseSearchDto);
        int count = super.count(baseSearchDto);
        log.info("-----end-----");
        return new BaseData(count, baseEntities);
    }

    public List<ProductDto> findProductByKeyword(BaseSearchDto baseSearchDto, Long categoryId, Integer id) throws SQLException {
        List<ProductDto> productDtoList = new ArrayList<>();
        String sql = this.getSearchSQL(baseSearchDto, categoryId, id);
            sql += " order by p.id desc ";
        sql += " limit ? offset ?";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        int index = 1;
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
            }
        }
        if (!DataUtil.isNullOrZero(categoryId)) {
            preparedStatement.setLong(index++, categoryId);
        }
        if (!DataUtil.isNullObject(id)) {
            preparedStatement.setInt(index++, id);
        }
        preparedStatement.setInt(index++, baseSearchDto.getSize());
        preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            ProductDto productDto = new ProductDto();
            productDto.setId(rs.getInt("id"));
            productDto.setImageUrl(rs.getString("imageUrl"));
            productDto.setProductCode(rs.getString("productCode"));
            productDto.setProductName(rs.getString("productName"));
            productDto.setPrice(rs.getLong("price"));
            productDto.setQuantity(rs.getInt("quantity"));
            productDto.setDescription(rs.getString("description"));
            productDto.setCategoryName(rs.getString("categoryName"));
            productDto.setCategoryId(rs.getInt("categoryId"));
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public Integer countProduct(BaseSearchDto baseSearchDto, Long categoryId, Integer id) throws SQLException {
        String sql = " SELECT count(1) from (" + getSearchSQL(baseSearchDto, categoryId, id) + ") as countLst ";
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        int index = 1;
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
            }
        }
        if (!DataUtil.isNullOrZero(categoryId)) {
            preparedStatement.setLong(index++, categoryId);
        }
        if (!DataUtil.isNullObject(id)) {
            preparedStatement.setLong(index++, id);
        }
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return 0;
    }

    public ProductDto getDetailProduct(BaseSearchDto baseSearchDto, Long categoryId, Integer id) throws SQLException {
        String sql = this.getSearchSQL(baseSearchDto, categoryId, id);
        if (DataUtil.isNullObject(id)) {
            sql += " order by id desc ";
            sql += " limit ? offset ?";
        }
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        int index = 1;
        if (baseSearchDto != null) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
                preparedStatement.setString(index++, "%" + baseSearchDto.getKeyword() + "%");
            }
        }
        if (!DataUtil.isNullObject(categoryId)) {
            preparedStatement.setLong(index++, categoryId);
        }
        if (!DataUtil.isNullObject(id)) {
            preparedStatement.setInt(index++, id);
        }
        if (DataUtil.isNullObject(id)) {
            preparedStatement.setInt(index++, baseSearchDto.getSize());
            preparedStatement.setInt(index, (baseSearchDto.getPage() - 1) * baseSearchDto.getSize());
        }
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()) {
            ProductDto productDto = new ProductDto();
            productDto.setId(rs.getInt("id"));
            productDto.setImageUrl(rs.getString("imageUrl"));
            productDto.setProductCode(rs.getString("productCode"));
            productDto.setProductName(rs.getString("productName"));
            productDto.setPrice(rs.getLong("price"));
            productDto.setQuantity(rs.getInt("quantity"));
            productDto.setDescription(rs.getString("description"));
            productDto.setCategoryName(rs.getString("categoryName"));
            productDto.setCategoryId(rs.getInt("categoryId"));
            return productDto;
        }

        return null;
    }


    public String getSearchSQL(BaseSearchDto baseSearchDto, Long categoryId, Integer id) {
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT p.id,p.image_url as imageUrl,p.product_code as productCode,p.product_name as productName,p.price,p.quantity,p.description,c.name as categoryName,p.category_id as categoryId ");
        sb.append("  FROM product p ");
        sb.append("       left join category c on c.status = 1 and p.category_id = c.id ");
        sb.append(" WHERE p.status = 1 ");
        if (!DataUtil.isNullObject(baseSearchDto)) {
            if (baseSearchDto.getKeyword() != null && !baseSearchDto.getKeyword().isEmpty()) {
                sb.append(" AND ( p.product_code LIKE ? OR p.product_name LIKE ? ) ");
            }
        }
        if (!DataUtil.isNullOrZero(categoryId)) {
            sb.append(" AND p.category_id = ? ");
        }
        if (!DataUtil.isNullObject(id)) {
            sb.append(" AND p.id= ? ");
        }
        return sb.toString();
    }

    public int updateProduct(Boolean isCancel, ProductEntity productEntity) throws SQLException {
            StringBuilder sb = new StringBuilder("");
        sb.append("UPDATE product " + "   SET updated_date = CURRENT_TIMESTAMP , " + "       updated_by = ? ");
            if (!DataUtil.isNullObject(productEntity)) {
                if (!isCancel) {
                    if (!DataUtil.isNullOrEmpty(productEntity.getImageUrl())) sb.append(" ,image_url = ? ");
                    if (!DataUtil.isNullOrEmpty(productEntity.getProductName())) sb.append(" ,product_name = ? ");
                    if (!DataUtil.isNullOrZero(productEntity.getPrice())) sb.append(" ,price = ? ");
                    if (!DataUtil.isNullOrZero(productEntity.getQuantity())) sb.append(" ,quantity = ? ");
                    if (!DataUtil.isNullOrEmpty(productEntity.getDescription())) sb.append(" ,description = ? ");
                }
                if (!DataUtil.isNullObject(productEntity.getStatus())) sb.append(" ,status = ? ");
            }
            sb.append(" WHERE id = ? ");
        if (isCancel) sb.append(" AND status = 1 ");
        PreparedStatement preparedStatement = getConnection().prepareStatement(sb.toString());
            int index = 1;
            preparedStatement.setString(index++, productEntity.getUpdatedBy());
            if (!DataUtil.isNullObject(productEntity)) {
                if (!isCancel) {
                    if (!DataUtil.isNullOrEmpty(productEntity.getImageUrl()))
                        preparedStatement.setString(index++, productEntity.getImageUrl().trim());
                    if (!DataUtil.isNullOrEmpty(productEntity.getProductName()))
                        preparedStatement.setString(index++, productEntity.getProductName().trim());
                    if (!DataUtil.isNullOrZero(productEntity.getPrice()))
                        preparedStatement.setLong(index++, productEntity.getPrice());
                    if (!DataUtil.isNullOrZero(productEntity.getQuantity()))
                        preparedStatement.setInt(index++, productEntity.getQuantity());
                    if (!DataUtil.isNullOrEmpty(productEntity.getDescription()))
                        preparedStatement.setString(index++, productEntity.getDescription().trim());
                }
                if (!DataUtil.isNullObject(productEntity.getStatus())) preparedStatement.setInt(index++, productEntity.getStatus());
            }
            preparedStatement.setInt(index++, productEntity.getId());
            return preparedStatement.executeUpdate();
    }
}
