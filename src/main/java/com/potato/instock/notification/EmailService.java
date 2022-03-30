package com.potato.instock.notification;

import com.potato.instock.item.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class EmailService {
    private final NotificationConfig notificationConfig;
    @Autowired
    public EmailService(NotificationConfig notificationConfig) {
        this.notificationConfig = notificationConfig;
    }

    public void sendMailNotification(String recipient,
                                     List<Item> items) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", notificationConfig.getAuth());
        properties.put("mail.smtp.starttls.enable", notificationConfig.getTls());
        properties.put("mail.smtp.host", notificationConfig.getHost());
        properties.put("mail.smtp.port", notificationConfig.getPort());

        System.out.println(notificationConfig.toString());

        // Set up the server's email
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(notificationConfig.getEmail(), notificationConfig.getPassword());
            }
        });

        // Set up the subject and misc
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(notificationConfig.getEmail(), false));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("Your item(s) are in stock!");
        message.setSentDate(new Date());

        // Append all the in stock items into the body of the message
        StringBuilder body = new StringBuilder();
        items.forEach(item -> {
            String url = "";
            if (item.getWebsite().equals("amazon")) {
                url = "https://amazon.com/dp/" + item.getItemId();
            }

            body.append("Name: ").append(item.getName()).append("\n\nPrice: ").append(item.getPrice()).append("\n\nBuy here now: ").append(url).append("\n\n");
        });

        body.append("Sent from the In-Stock Potato Tracker :)");
        message.setText(body.toString());
        Transport.send(message);
    }
}
