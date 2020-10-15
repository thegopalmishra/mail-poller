package com.bourntec.mailpoller.flows.impl;

import java.io.IOException;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
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
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.dsl.Mail;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;
import com.bourntec.mailpoller.flows.ImapIntegrationFlow;

@Component("imapPollFlow")
public class ImapPollFlow implements ImapIntegrationFlow {


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


	@Override
	public IntegrationFlow integrationFlow(String email,String password){
		System.out.println("integrationFlow()");
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
										.put("mail.mime.allowutf8", "true")
										.put("mail.imap.connectionpoolsize", "5")
										.put("mail.imap.starttls.enable", "true")
										.put("mail.imap.ssl.trust", "*")
									), e -> e
										.poller(Pollers.fixedRate(5000)
											.maxMessagesPerPoll(1)))
				.<MimeMessage>handle((payload, header) -> {
					Object o = null;
					try {
						o= logMail(payload);
					} catch (IOException | javax.mail.MessagingException e1) {
						e1.printStackTrace();
					}
					return o;
				})
				.transform(Mail.toStringTransformer())
				.get();

	}


	private Object logMail(MimeMessage message) throws IOException, javax.mail.MessagingException {
		try {
			System.out.println("logMail()");
			System.out.println(message.getSubject());
			System.out.println(message.getContent().toString());
//			logger.info(message.getContent().toString());
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			ImapPollFlow.getContentFromMimeMultipart(mimeMultipart);
		} catch(ClassCastException ce) {
			System.out.println("Class cast exception");
			System.out.println(message.getContent().toString());
		}
		catch (Exception e) {
			System.out.println("### Read mail content error");
//			logger.error("Read mail content error", e);
		}
		return null;
	}

	public static String getContentFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException, javax.mail.MessagingException {
		System.out.println("getContentFromMimeMultipart()");
		String emailBody = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				emailBody = emailBody + "\n" + bodyPart.getContent();
			} else if (bodyPart.isMimeType("text/html")) {
				emailBody = emailBody + "\n" + bodyPart.getContent();
			} else if (bodyPart.getContent() instanceof MimeMultipart){
				emailBody = emailBody + getContentFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		System.out.println(emailBody);
		return emailBody;
	}

}
