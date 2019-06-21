import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class SendEmail {

    final private static String user = "master@mail.ru";
    final private static String host = "localhost";
    final private static String password = "master";

    public static Status send(String email, String registrationToken) {

        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", 2500);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user,password);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(email));
            message.setSubject("Complete registration");
            message.setText("To complete registration you need to enter the token below \n\n" + registrationToken);

            //send the message
            Transport.send(message);

            System.out.printf("Message to %s was sent successfully\n", email);
            return Status.OK;
        } catch (MessagingException e) {
            return Status.NO_MAIL;
        }
    }
}