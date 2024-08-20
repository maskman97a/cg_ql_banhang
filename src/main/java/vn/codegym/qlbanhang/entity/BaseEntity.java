package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;
import vn.codegym.qlbanhang.constants.Const;
import vn.codegym.qlbanhang.utils.ClassUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class BaseEntity {
    @Column(name = "id")
    protected Integer id;
    @Column(name = "status")
    protected int status;
    @Column(name = "created_by")
    protected String createdBy;
    @Column(name = "created_date")
    protected LocalDateTime createdDate;
    @Column(name = "updated_by")
    protected String updatedBy;
    @Column(name = "updated_date")
    protected LocalDateTime updatedDate;

    @Setter
    @Getter
    private Map<String, Object> mapValue;

    public BaseEntity() {
        this.status = Const.STATUS_ACTIVE;
        this.createdBy = "admin";
        this.createdDate = LocalDateTime.now();
        this.updatedBy = "admin";
        this.updatedDate = LocalDateTime.now();
    }

    public static BaseEntity getInstance(String tableName) {
        try {
            List<Class<?>> classes = ClassUtils.getClasses("vn.codegym.qlbanhang.entity");
            for (Class<?> clazz : classes) {
                if (clazz.getAnnotation(Table.class) != null) {
                    Table table = clazz.getAnnotation(Table.class);
                    if (table.name().equals(tableName)) {
                        return (BaseEntity) clazz.getDeclaredConstructor().newInstance();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
