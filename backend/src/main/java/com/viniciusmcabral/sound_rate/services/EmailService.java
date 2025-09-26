package com.viniciusmcabral.sound_rate.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;

@Service
public class EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private SendGrid sendGridClient;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${app.sendgrid.from-email}")
	private String fromEmail;

	@Async
	public void sendWelcomeEmail(String to, String username) {
		Context context = new Context();
		context.setVariable("username", username);
		String htmlContent = templateEngine.process("welcome-email", context);

		Email from = new Email(fromEmail, "SoundRate");
		Email toEmail = new Email(to);
		String subject = "Welcome to SoundRate!";
		Content content = new Content("text/html", htmlContent);
		Mail mail = new Mail(from, subject, toEmail, content);

		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sendGridClient.api(request);
			logger.info("Welcome email sent to {}.", to);
		} catch (IOException ex) {
			logger.error("Error sending welcome email to {}: {}", to, ex.getMessage());
		}
	}

	@Async
	public void sendAccountDeletionEmail(String to, String username) {
		Context context = new Context();
		context.setVariable("username", username);
		String htmlContent = templateEngine.process("deletion-email", context);

		Email from = new Email(fromEmail, "SoundRate");
		Email toEmail = new Email(to);
		String subject = "Your SoundRate Account Has Been Deleted";
		Content content = new Content("text/html", htmlContent);
		Mail mail = new Mail(from, subject, toEmail, content);

		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sendGridClient.api(request);
			logger.info("Account deletion email sent to {}.", to);
		} catch (IOException ex) {
			logger.error("Error sending account deletion email to {}: {}", to, ex.getMessage());
		}
	}

	@Async
	public void sendPasswordResetEmail(String to, String username, String resetLink) {
		Context context = new Context();
		context.setVariable("username", username);
		context.setVariable("resetLink", resetLink);
		String htmlContent = templateEngine.process("password-reset-email", context);

		Email from = new Email(fromEmail, "SoundRate");
		Email toEmail = new Email(to);
		String subject = "SoundRate - Password Reset Request";
		Content content = new Content("text/html", htmlContent);
		Mail mail = new Mail(from, subject, toEmail, content);

		Request request = new Request();

		try {
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());

			Response response = sendGridClient.api(request);

			logger.info("Email sent to {}. Status Code: {}", to, response.getStatusCode());

		} catch (IOException ex) {
			logger.error("Error sending email to {}: {}", to, ex.getMessage());
		}
	}
}