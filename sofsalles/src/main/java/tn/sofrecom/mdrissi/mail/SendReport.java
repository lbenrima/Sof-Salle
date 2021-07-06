package tn.sofrecom.mdrissi.mail;

import java.io.IOException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class SendReport {
	
	@Value("${mail.smtp.host}")
	private String host;
	
	@Value("${mail.smtp.port}")
	private String port;
	
	@Value("${mail.smtp.starttls.enable}")
	private String starttls;
	
	public SendReport() {};

    public void SendReportMail(String from, String subject, String text) throws IOException {

    	Properties prop = new Properties();
    	prop.put("mail.smtp.starttls.enable", starttls);
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);     		        	
        Session session = Session.getDefaultInstance(prop,null); 
        String to = "softun.digitalisation@sofrecom.com";
        String fullSubject = "[SofSalles] " + subject;
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            
            message.setSubject(fullSubject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}