package com.circles.health.patient.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.circles.health.patient.constants.PatientMessages;


public class CircleHealthEmail {

	public Properties getMailProperties() {
		
		String host="smtp.gmail.com";
		
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES "+properties);
		
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		return properties;
		
	}

	public EmailAuthenticator getEmailAuthenticator() {
		return new EmailAuthenticator();
	}
	
	public String forgottenAccessCode(String to,String accessCode) {
		String from = PatientMessages.FROM_EMAIL;
		String subject = PatientMessages.FORGOTTEN_CODE_EMAIL_SUBJECT;
		String message = PatientMessages.FORGOTTEN_CODE_EMAIL + accessCode;
		
		Session session=Session.getInstance(this.getMailProperties(), this.getEmailAuthenticator());
		session.setDebug(true);
		
		MimeMessage m = new MimeMessage(session);
		
		try {
		
		m.setFrom(from);
		
		m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		m.setSubject(subject);
	
		m.setText(message);
		
		Transport.send(m);
		
		System.out.println("Access Code send successfully..................");
		
		return "Access Code send successfully.";
		
		}catch (Exception e) {
			e.printStackTrace();
			return "Some Exception occur while sending access code...........";
		}
	}
	
	public String sendAccessCode(String to,String accessCode) {
		String from = PatientMessages.FROM_EMAIL;
		String subject = PatientMessages.USER_CODE_EMAIL_SUBJECT;
		String message = PatientMessages.USER_CODE_EMAIL + accessCode;
		
		Session session=Session.getInstance(this.getMailProperties(), this.getEmailAuthenticator());
		session.setDebug(true);
		
		MimeMessage m = new MimeMessage(session);
		
		try {
		
		m.setFrom(from);
		
		m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
		m.setSubject(subject);
	
		m.setText(message);
		
		Transport.send(m);
		
		System.out.println("Access Code send successfully..................");
		
		return "Access Code send successfully.";
		
		}catch (Exception e) {
			e.printStackTrace();
			return "Some Exception occur while sending access code...........";
		}
	}
}
