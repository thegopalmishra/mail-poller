package com.bourntec.mailpoller.flows.impl;

import java.io.IOException;

import javax.jms.Queue;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.bourntec.mailpoller.flows.ImapIntegrationFlow;
import com.bourntec.mailpoller.flows.utils.MailProcessor;


@Component("imapIdleFlow")
public class ImapIdleFlow  implements ImapIntegrationFlow {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MailProcessor mailProcessor;
	
	@Autowired
	@Qualifier("imapURL")
	private String imapURL;

	@Autowired
	@Qualifier("mailHeaderMapper")
	public HeaderMapper<MimeMessage> mailHeaderMapper;

	@Autowired
	@Qualifier("searchTerm")
	public SearchTerm defaultSearchTerm;

    
//	@Autowired
//    JmsTemplate jmsTemplate;
//
//    @Autowired
//    Queue persistQueue;
    

    @Autowired
    Queue saveQueue;

	public IntegrationFlow integrationFlow(String email,String password){
		logger.info("integrationFlow()");
		System.out.println("integrationFlow()");
		return IntegrationFlows
				.from(Mail.imapIdleAdapter(imapURL)
						.autoStartup(true)
						.shouldMarkMessagesAsRead(true)
						.shouldDeleteMessages(false)
						.searchTermStrategy((a, b) -> defaultSearchTerm)
						.javaMailAuthenticator(new Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(email, password);
							}
						})
						.shouldReconnectAutomatically(true)
						.autoCloseFolder(false)
//						.headerMapper(mailHeaderMapper)
						)
				.handle(new MessageHandler() {
					@Override
					public void handleMessage(Message<?> message) throws MessagingException{
						System.out.println("handleMessage()");
//						jmsTemplate.convertAndSend(saveQueue, ((javax.mail.internet.MimeMessage) message.getPayload()));
						try {
							mailProcessor.logMail(((javax.mail.internet.MimeMessage) message.getPayload()));
						} catch (IOException | javax.mail.MessagingException e) {
							System.out.println("IOException | javax.mail.MessagingException occurred");
							e.printStackTrace();
						}
//						logger.info(message.getPayload().toString());
						System.out.println(message.getPayload().toString());
					}
				})
				.transform(Mail.toStringTransformer())
				.get();
	}

}
