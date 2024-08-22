package vn.codegym.qlbanhang.model;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.entity.EmailEntity;

import java.time.LocalDateTime;

@Getter
@Setter
public class EmailModel extends BaseModel {
    private static final EmailModel inst = new EmailModel();

    private EmailModel() {
        super(EmailEntity.class);
    }

    public static EmailModel getInstance() {
        return inst;
    }
}
