package com.lxs.common.plugins;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailPlugin {

	public void sendMail(String to, String text, String theme) throws MessagingException {
		
		

		Properties properties = new Properties();
		
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "25");
		properties.put("mail.smtp.host", "smtp.126.com");

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("zhanghjqqqq@126.com", "QQ2662065155");
			}
		});

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("zhanghjqqqq@126.com"));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(theme);
		message.setContent(text, "text/html; charset=UTF-8");
		
		Transport.send(message);
	}

}
