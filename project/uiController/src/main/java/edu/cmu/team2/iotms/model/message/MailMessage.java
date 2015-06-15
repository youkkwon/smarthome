package edu.cmu.team2.iotms.model.message;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import edu.cmu.team2.iotms.application.UserServiceImpl;
import edu.cmu.team2.iotms.conf.SpringAppConfig;
import edu.cmu.team2.iotms.domain.UserInfo;

public class MailMessage extends IoTMSMessage {
	private UserServiceImpl userService;
	
	private JavaMailSenderImpl mailSender;
	
	public MailMessage() {
		mailSender = new JavaMailSenderImpl();
		
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);
		mailSender.setUsername("swarchitect.cmu@gmail.com");
		mailSender.setPassword("cmuteam@");
		Properties javaMailProperties = mailSender.getJavaMailProperties();
		javaMailProperties.setProperty("mail.smtp.auth", "true");
		javaMailProperties.setProperty("mail.smtp.starttls.enable", "true");    
		mailSender.setJavaMailProperties(javaMailProperties);
		
		SpringAppConfig sac = new SpringAppConfig();
		userService = (UserServiceImpl) sac.userService();
	}
	
	private String[] getReceiver() {
		List<UserInfo> users = userService.getUsers();
		ArrayList<String> recevier = new ArrayList<String>();
		for(UserInfo user:users) {
			String mail = user.getUserMail();
			if(mail != null && mail.compareTo("") != 0)
				recevier.add(mail);
		}
		
		String[] strArr = new String[recevier.size()];
		strArr = recevier.toArray(strArr);
		
		return strArr;
	}

	@Override
	protected void sendConfirmMessage() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void sendEmergencyMessage() {
		String[] address = getReceiver();
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("[IoTMS] This is a Emergency Message");
		message.setFrom("no-reply@iotms.com");
		message.setText("Emergency");
		message.setTo(address);
		try {
			mailSender.send(message);
		} catch(MailException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void sendMalFunctionMessage() {
		String[] address = getReceiver();
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("[IoTMS] This is a MalFunction Message");
		message.setFrom("no-reply@iotms.com");
		message.setText("Emerge mulfunction on system.");
		message.setTo(address);
		try {
			mailSender.send(message);
		} catch(MailException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void sendPostMessage() {
		String[] address = getReceiver();
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setSubject("[IoTMS] You've got a mail in mailbox");
		message.setFrom("hanbell@gmail.com");
		message.setText("Check your mailbox in your house.");
		message.setTo(address);
		try {
			mailSender.send(message);
		} catch(MailException e) {
			e.printStackTrace();
		}
	}

}
