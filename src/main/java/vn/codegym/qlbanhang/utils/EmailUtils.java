package vn.codegym.qlbanhang.utils;


import vn.codegym.qlbanhang.config.PropertiesConfig;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailUtils {
    private static final String MAIL_USERNAME = System.getenv("MAIL_USERNAME");
    private static final String MAIL_PASSWORD = System.getenv("MAIL_PASSWORD");
    private static final String EMAIL_SMTP = PropertiesConfig.getProperty("email.smtp");
    private static final String EMAIL_SMTP_PORT = PropertiesConfig.getProperty("email.smtp.port");
    private static final String EMAIL_SMTP_AUTH = PropertiesConfig.getProperty("email.smtp.auth");
    private static final String EMAIL_SMTP_STARTTLS_ENABLE = PropertiesConfig.getProperty("email.smtp.starttls.enable");


    public static Session getSession() {
        // Sender's email ID and password need to be mentioned
        final String from = MAIL_USERNAME;
        final String password = MAIL_PASSWORD;
        // Setting up configurations for the email connection to the Google SMTP server using TLS
        Properties properties = new Properties();
        properties.put("mail.smtp.host", EMAIL_SMTP);
        properties.put("mail.smtp.port", EMAIL_SMTP_PORT);
        properties.put("mail.smtp.auth", EMAIL_SMTP_AUTH);
        properties.put("mail.smtp.starttls.enable", EMAIL_SMTP_STARTTLS_ENABLE);

        // Create a session with an authenticator
        return javax.mail.Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
    }

    public static boolean sendEmail(String to, String subject, String htmlContent) {
        Session session = getSession();

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header
            message.setFrom(new InternetAddress(MAIL_USERNAME));

            // Set To: header field of the header
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject(subject);


            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);

            message.setContent(multipart);
            // Send message
            Transport.send(message);

            System.out.println("Send mail Successfully!");
            return true;
        } catch (AddressException e) {
            System.out.println(e.getMessage());
        } catch (MessagingException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
