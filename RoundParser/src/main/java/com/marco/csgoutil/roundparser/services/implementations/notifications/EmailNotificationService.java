package com.marco.csgoutil.roundparser.services.implementations.notifications;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.marco.csgoutil.roundparser.services.interfaces.NotificationService;

/**
 * This implementations will send email notifications
 * 
 * @author Marco
 *
 */
public class EmailNotificationService implements NotificationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationService.class);
	
	@Value("${com.marco.csgoutil.notification.enabled}")
	private boolean notificationEnabled;
	@Value("${spring.mail.username}")
    private String emailFrom;
    @Autowired
    private JavaMailSender emailSender;
	
	@Override
	public void sendParsingCompleteNotification(List<String> recipients, String title, String message) {
		if(!notificationEnabled) {
			LOGGER.info("Notification not enabled");
			return;
		}
		SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(emailFrom);
        email.setTo(recipients.toArray(new String[recipients.size()]));
        email.setSubject(title);
        email.setText(message);
        emailSender.send(email);
        LOGGER.info("Notification sent");

	}

}
