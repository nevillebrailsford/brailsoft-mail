package com.brailsoft.mail;

import java.time.LocalDate;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.brailsoft.base.ApplicationConfiguration;
import com.brailsoft.base.IniFile;
import com.brailsoft.base.Notification;
import com.brailsoft.base.NotificationCentre;

public class EmailSender implements Runnable {
	private static final String CLASS_NAME = EmailSender.class.getName();
	public final static Logger LOGGER = ApplicationConfiguration.logger();

	private String text;

	public EmailSender(String text) {
		this.text = text;
	}

	@Override
	public void run() {
		LOGGER.entering(CLASS_NAME, "run");
		EmailConfigurer configurer = EmailConfigurer.getInstance();
		if (configurer.isValidConfiguration()) {
			Session session = configurer.getSession();
			Message message = new MimeMessage(session);
			try {
				String recipients = MailPreferences.mailingList();
				message.setFrom(new InternetAddress(configurer.userName()));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
				message.setSubject("Property Managment Report");
				message.setText(text);
				Transport.send(message);
				Notification notification = new Notification(MailNotificationType.Sent, this,
						"Email sent successfully");
				NotificationCentre.broadcast(notification);
				LocalDate now = LocalDate.now();
				IniFile.store(MailConstants.DATE_OF_LAST_EMAIL, now.toString());
			} catch (Exception e) {
				LOGGER.warning("Caught exception: " + e.getMessage());
				Notification notification = new Notification(MailNotificationType.Failed, this,
						"Email send attempt failed");
				NotificationCentre.broadcast(notification);
			}
		} else {
			LOGGER.warning("EmailConfiguration not valid");
		}
		LOGGER.exiting(CLASS_NAME, "run");
	}

}
