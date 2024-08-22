package vn.codegym.qlbanhang.entity;

import lombok.Getter;
import lombok.Setter;
import vn.codegym.qlbanhang.annotation.Column;
import vn.codegym.qlbanhang.annotation.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "email")
public class EmailEntity extends BaseEntity {
    @Column(name = "receiver")
    private String receiver;
    @Column(name = "mail_title")
    private String mailTitle;
    @Column(name = "mail_body")
    private byte[] mailBody;
    @Column(name = "send_date")
    private LocalDateTime sendDate;
    @Column(name = "retry")
    private int retry;

}
