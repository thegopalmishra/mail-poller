package com.bourntec.mailpoller.activemq.listener;

import java.io.IOException;
import java.util.HashMap;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.bourntec.mailpoller.flows.utils.MailProcessor;

@Component
public class Consumer {

	@Autowired
	MailProcessor mailProcessor;
	
    @JmsListener(destination = "mailpoller.saveQueue")
    public void saveFiles(MimeMessage message) throws IOException, MessagingException {
//  public void saveFiles(byte[] msgBytes) throws IOException, MessagingException {
    	mailProcessor.logMail(message);
//    	HashMap<String,String> email;
//    	email = (HashMap<String, String>) mailProcessor.processMimeMessage(message);
//			
//    	jmsTemplate.convertAndSend(persistQueue, email);
    }
    
    @JmsListener(destination = "mailpoller.persistQueue")
    public void persistToDB(HashMap<String,String> email) {
    	System.out.println("mailpoller.persistQueue - Consumer");
    	mailProcessor.printEmail(email);
    	mailProcessor.saveEmail(email);
    	
    }
}