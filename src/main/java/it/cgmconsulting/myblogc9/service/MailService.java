package it.cgmconsulting.myblogc9.service;

import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import it.cgmconsulting.myblogc9.utils.Mail;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {
	
	@Value("${mail.sender}")
	String from;
	
	@Autowired JavaMailSender javaMailSender;
	
	public void sendMail(Mail mail) {
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(mail.getMailFrom());
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setText(mail.getMailContent());
			
			javaMailSender.send(mimeMessage);
			log.info(mail.toString());
			
		} catch (MessagingException e) {
			log.error("ERROR SENDING EMAIL");
			e.printStackTrace();
			
		}
	}
	
	public Mail sendTrend(String to, String report) {
		Mail m = new Mail();
		m.setMailFrom(from);
		m.setMailTo(to);
		m.setMailSubject("Registration Trend");
		m.setMailContent(report);
		return m;
	}
}

