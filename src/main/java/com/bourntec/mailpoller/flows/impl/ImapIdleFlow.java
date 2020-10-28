package com.bourntec.mailpoller.flows.impl;

import java.io.IOException;

import javax.jms.Queue;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.bourntec.mailpoller.flows.ImapIntegrationFlow;
import com.bourntec.mailpoller.flows.utils.MailProcessor;

/**
 * @author Gopal
 *
 */
@Component("imapIdleFlow")
public class ImapIdleFlow  implements ImapIntegrationFlow {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MailProcessor mailProcessor;
	
	@Autowired
	@Qualifier("imapURL")
	private String imapURL;

	@Autowired
	@Qualifier("searchTerm")
	public SearchTerm defaultSearchTerm;



    @Autowired
    Queue saveQueue;

	public IntegrationFlow integrationFlow(String email,String password){
		logger.info("Entered: Method: integrationFlow(), Class: ImapIdleFlow");
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
						)
				.handle(new MessageHandler() {
					@Override
					public void handleMessage(Message<?> message) throws MessagingException{
						logger.info("Entered: Method: handleMessage(), Class: ImapIdleFlow");
						try {
							logger.debug("Message: {}", message.getPayload().toString());
							mailProcessor.postToProcessQueue(((javax.mail.internet.MimeMessage) message.getPayload()));
						} catch (IOException | javax.mail.MessagingException e) {
							logger.error("IOException | javax.mail.MessagingException occurred");
							logger.debug("StackTrace: {}", e.getMessage());
						}
						logger.info("Exited: Method: handleMessage(), Class: ImapIdleFlow");
					}
				})
				.transform(Mail.toStringTransformer())
				.get();
	}

}
