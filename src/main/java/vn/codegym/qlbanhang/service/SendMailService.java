package vn.codegym.qlbanhang.service;

import vn.codegym.qlbanhang.model.EmailModel;

public class SendMailService {
    public static final SendMailService inst = new SendMailService();
    private final EmailModel emailModel;

    private SendMailService() {
        this.emailModel = EmailModel.getInstance();
    }



}
