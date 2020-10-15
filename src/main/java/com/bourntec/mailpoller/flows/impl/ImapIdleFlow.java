package com.bourntec.mailpoller.flows.impl;

import java.io.IOException;

import javax.mail.Authenticator;
//import javax.mail.BodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
//import org.springframework.integration.mail.dsl.ImapIdleChannelAdapterSpec;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import com.bourntec.mailpoller.flows.ImapIntegrationFlow;


@Component("imapIdleFlow")
public class ImapIdleFlow  implements ImapIntegrationFlow {

	private Logger logger = LoggerFactory.getLogger(getClass());


	@Autowired
	@Qualifier("imapURL")
	private String imapURL;

	@Autowired
	@Qualifier("mailHeaderMapper")
	public HeaderMapper<MimeMessage> mailHeaderMapper;

	@Autowired
	@Qualifier("searchTerm")
	public SearchTerm defaultSearchTerm;



	public IntegrationFlow integrationFlow(String email,String password){
		//		ImapIdleChannelAdapterSpec spec = ;
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
						MimeMessage msg = ((javax.mail.internet.MimeMessage) message.getPayload());
						try {
							MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
							ImapPollFlow.getContentFromMimeMultipart(mimeMultipart);
						} catch (MessagingException | IOException | javax.mail.MessagingException e) {
							System.out.println("Messaging Exception | IOException");
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
