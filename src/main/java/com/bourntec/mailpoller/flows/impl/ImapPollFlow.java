package com.bourntec.mailpoller.flows.impl;

import static com.bourntec.mailpoller.utils.ConstantLiterals.PROPERTY_MAIL_IMAP_CONNECTION_POOL_SIZE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.PROPERTY_MAIL_IMAP_SSL_TRUST;
import static com.bourntec.mailpoller.utils.ConstantLiterals.PROPERTY_MAIL_IMAP_START_TLS_ENABLE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.PROPERTY_MAIL_MIME_ALLOW_UTF_8;
import static com.bourntec.mailpoller.utils.ConstantLiterals.STRING_VALUE_CONNECTION_POOL_SIZE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.STRING_VALUE_TRUE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.STRING_VALUE_WILDCARD;

import java.io.IOException;

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
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.stereotype.Component;

import com.bourntec.mailpoller.flows.ImapIntegrationFlow;
import com.bourntec.mailpoller.flows.utils.MailProcessor;

/**
 * @author Gopal
 *
 */
@Component("imapPollFlow")
public class ImapPollFlow implements ImapIntegrationFlow {


	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MailProcessor mailProcessor;

	@Autowired
	@Qualifier("imapURL")
	private String imapURL;


	@Autowired
	@Qualifier("searchTerm")
	public SearchTerm defaultSearchTerm;



	@Override
	public IntegrationFlow integrationFlow(String email,String password){
		logger.info("Entered: Method: integrationFlow(), Class: ImapPollFlow");
		return IntegrationFlows
				.from(Mail.imapInboundAdapter(imapURL)
						.shouldMarkMessagesAsRead(true)
						.shouldDeleteMessages(false)
						.autoCloseFolder(false)
						.searchTermStrategy((a, b) -> defaultSearchTerm)
						.javaMailAuthenticator(new Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {
								return new PasswordAuthentication(email, password);
							}
						})
						.javaMailProperties(p -> p
								.put(PROPERTY_MAIL_MIME_ALLOW_UTF_8, STRING_VALUE_TRUE)
								.put(PROPERTY_MAIL_IMAP_CONNECTION_POOL_SIZE, STRING_VALUE_CONNECTION_POOL_SIZE)
								.put(PROPERTY_MAIL_IMAP_START_TLS_ENABLE, STRING_VALUE_TRUE)
								.put(PROPERTY_MAIL_IMAP_SSL_TRUST, STRING_VALUE_WILDCARD)
								), e -> e
						.poller(Pollers.fixedRate(5000).maxMessagesPerPoll(1)))
				.<MimeMessage>handle((payload, header) -> {
					logger.info("Entered: Method: handleMessage(), Class: ImapPollFlow");
					try {
						logger.debug("Message: {}", payload.toString());
						mailProcessor.postToProcessQueue(payload);
					} catch (IOException | javax.mail.MessagingException e) {
						logger.error("IOException | javax.mail.MessagingException occurred");
						logger.debug("StackTrace: {}", e.getMessage());
					}
					logger.info("Exited: Method: handleMessage(), Class: ImapPollFlow");
					return null; //Need to check why its needed.
				})
				.transform(Mail.toStringTransformer())
				.get();

	}




}
