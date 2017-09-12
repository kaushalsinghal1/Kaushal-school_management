package com.framework.platform.mail;

import java.security.Security;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendMail {

	private String[] to;
	private String subject;
	private String text;
	private String from;
	private String password;
	private String[] attachmentFiles;

	public SendMail(String from, String password, String[] to, String subject,
			String text, String[] attachmentFiles) {
		this.from = from;
		this.password = password;
		this.to = to;
		this.subject = subject;
		this.text = text;
		this.attachmentFiles = attachmentFiles;
	}

	public void send() throws NoSuchProviderException, AddressException {

		try {
			Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
			Properties props = new Properties();
			props.setProperty("mail.transport.protocol", "smtp");
			props.setProperty("mail.host", "smtp.gmail.com");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			props.put("mail.debug", "true");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.socketFactory.fallback", "false");
			Session session = Session.getDefaultInstance(props,
					new GJMailAuthenticator(from, password));
			session.setDebug(true);
			Transport transport = session.getTransport();
			// InternetAddress addressFrom=new
			// InternetAddress("sender@gmail.com");

			InternetAddress addressFrom = new InternetAddress(from);

			MimeMessage message = new MimeMessage(session);
			

			InternetAddress[] addressTo = new InternetAddress[to.length];

			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
			message.setSender(addressFrom);
			message.setSubject(subject);
			message.setContent(text, "text/plain");
			message.setRecipients(Message.RecipientType.TO, addressTo);
			
			//new
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(text);
			Multipart multipart = new MimeMultipart();

			// add the message body to the mime message
			multipart.addBodyPart(messageBodyPart);

			// add any file attachments to the message
			if (attachmentFiles != null && attachmentFiles.length > 0) {
				addAtachments(multipart);
			}
			// Put all message parts in the message
			message.setContent(multipart);
			transport.connect();
			Transport.send(message);
			transport.close();

		//	System.out.println("DONE");

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	protected void addAtachments(Multipart multipart)
			throws MessagingException, AddressException {
		for (int i = 0; i <= attachmentFiles.length - 1; i++) {
			String filename = attachmentFiles[i];
			MimeBodyPart attachmentBodyPart = new MimeBodyPart();
			// use a JAF FileDataSource as it does MIME type detection
			DataSource source = new FileDataSource(filename);
			attachmentBodyPart.setDataHandler(new DataHandler(source));
			attachmentBodyPart.setFileName(filename);
			// add the attachment
			multipart.addBodyPart(attachmentBodyPart);
		}
	}
}

class GJMailAuthenticator extends javax.mail.Authenticator {
	private String password = "";
	private String from = "";

	public GJMailAuthenticator(String from, String password) {
		this.from = from;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(from, password);

	}
}
