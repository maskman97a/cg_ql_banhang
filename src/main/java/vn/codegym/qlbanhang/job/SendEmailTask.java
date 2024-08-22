package vn.codegym.qlbanhang.job;

import vn.codegym.qlbanhang.dto.BaseSearchDto;
import vn.codegym.qlbanhang.dto.QueryConditionDto;
import vn.codegym.qlbanhang.entity.BaseEntity;
import vn.codegym.qlbanhang.entity.EmailEntity;
import vn.codegym.qlbanhang.model.EmailModel;
import vn.codegym.qlbanhang.utils.EmailUtils;

import java.util.Base64;
import java.util.List;

public class SendEmailTask implements Runnable {
    public static final SendEmailTask inst = new SendEmailTask();
    private EmailModel emailModel;

    public SendEmailTask() {
        this.emailModel = EmailModel.getInstance();
    }

    public static SendEmailTask getInstance() {
        return inst;
    }

    @Override
    public void run() {
        try {
            BaseSearchDto baseSearchDto = new BaseSearchDto();
            QueryConditionDto statusCondition = QueryConditionDto.newAndCondition("status", "=", "0");
            QueryConditionDto retryCondition = QueryConditionDto.newAndCondition("retry", "<", 5);
            baseSearchDto.getQueryConditionDtos().add(statusCondition);
            baseSearchDto.getQueryConditionDtos().add(retryCondition);
            List<BaseEntity> entities = emailModel.search(baseSearchDto);
            for (BaseEntity baseEntity : entities) {
                EmailEntity emailEntity = (EmailEntity) baseEntity;
                boolean result = EmailUtils.sendEmail(emailEntity.getReceiver(), emailEntity.getMailTitle(), new String(Base64.getDecoder().decode(emailEntity.getMailBody())));
                if (result) {
                    emailEntity.setStatus(1);
                    emailEntity.setRetry(emailEntity.getRetry() + 1);
                } else {
                    emailEntity.setRetry(emailEntity.getRetry() + 1);
                }
                emailModel.save(emailEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
