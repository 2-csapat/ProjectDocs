package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * Our mail sender utility class
 */
public class MailUtil {
    /**
     *
     * @param toEmail the email address to send
     * @param subject the subject of the email
     * @param body    the text of the email
     */
    public void sendMail(String toEmail, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        final String fromEmail = "villaskulcs234@gmail.com";
        final String password = "Vvillaskulcs234!";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        properties.put("mail.smtp.port", "587"); //TLS Port
        properties.put("mail.smtp.auth", "true"); //enable authentication
        properties.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator authenticator = new Authenticator() {
            //override the password authentication method
            protected PasswordAuthentication getPasswordAuthentication () {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(properties, authenticator);

        MimeMessage msg = new MimeMessage(session);
        //set message headers
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply-IBANK"));
        msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
        msg.setSubject(subject, "UTF-8");
        msg.setText(body, "UTF-8");
        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
        System.out.println("Message is ready");
        Transport.send(msg);
    }
}
