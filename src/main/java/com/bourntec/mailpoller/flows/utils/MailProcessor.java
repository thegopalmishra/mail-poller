/**
 * 
 */
package com.bourntec.mailpoller.flows.utils;

import static com.bourntec.mailpoller.utils.ConstantLiterals.ATTACHMENT_COUNT;
import static com.bourntec.mailpoller.utils.ConstantLiterals.ATTACHMENT_NAMES;
import static com.bourntec.mailpoller.utils.ConstantLiterals.CONTENT_TYPE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_BODY;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_FROM;
import static com.bourntec.mailpoller.utils.ConstantLiterals.SEPARATOR_KEYWORD;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_SENT_DATE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_SUBJECT;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_TO;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMPTY_STRING;
import static com.bourntec.mailpoller.utils.ConstantLiterals.HAS_ATTACHMENT;
import static com.bourntec.mailpoller.utils.ConstantLiterals.MIME_TYPE_TEXT_HTML;
import static com.bourntec.mailpoller.utils.ConstantLiterals.MIME_TYPE_TEXT_PLAIN;
import static com.bourntec.mailpoller.utils.ConstantLiterals.MIME_TYPE_MULTIPART;
import static com.bourntec.mailpoller.utils.ConstantLiterals.SEPARATOR_NEW_LINE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.PROPERTY_MAIL_HOST;
import static com.bourntec.mailpoller.utils.ConstantLiterals.PROPERTY_MAIL_TRANSPORT_PROTOCOL;
import static com.bourntec.mailpoller.utils.ConstantLiterals.ATTACHMENT_STORAGE_PATH;
import static com.bourntec.mailpoller.utils.ConstantLiterals.STRING_VALUE_TRANSPORT_PROTOCOL;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.jms.Queue;
import javax.mail.BodyPart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.fusesource.hawtbuf.ByteArrayInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import com.bourntec.mailpoller.service.EmailStoreService;
import com.bourntec.mailpoller.utils.DateUtil;
import com.bourntec.mailpoller.utils.GenericUtils;

/**
 * @author Gopal
 *
 */
@Component("mailProcessor")
public class MailProcessor {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private EmailStoreService emailStoreService;
	
    @Autowired
    JmsTemplate jmsTemplate;
    
    @Autowired
    Queue saveQueue;

    @Autowired
    Queue persistQueue;
    
    @Autowired
    String server;
    
    public void postToProcessQueue(MimeMessage message) throws IOException, javax.mail.MessagingException {
    	logger.info("Entered: Method: postToProcessQueue(), Class: MailProcessor");
    	final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		message.writeTo(outputStream);
		jmsTemplate.convertAndSend(saveQueue, outputStream.toByteArray());
		logger.info("Exited: Method: postToProcessQueue(), Class: MailProcessor");
    }
    
    public MimeMessage toMimeMessage(byte[] rawMessageBytes) {
    	logger.info("Entered: Method: postToProcessQueue(), Class: MailProcessor");
    	final String MAIL_HOST = server.split("/")[2].split(":")[0];
    	Properties props = System.getProperties();  
		props.put(PROPERTY_MAIL_HOST, MAIL_HOST);  
		props.put(PROPERTY_MAIL_TRANSPORT_PROTOCOL, STRING_VALUE_TRANSPORT_PROTOCOL); 
		Session mailSession = Session.getDefaultInstance(props, null);  
		InputStream source = new ByteArrayInputStream(rawMessageBytes); 
		MimeMessage message = null;;
		try {
			message = new MimeMessage(mailSession, source);  
		} catch(javax.mail.MessagingException e) {
			logger.error("javax.mail.MessagingException occurred");
			logger.debug("StackTrace: {}", e.getMessage());
		}
		logger.info("Exited: Method: postToProcessQueue(), Class: MailProcessor");
		return message;
    }

	public void saveEmail(Map<String, String> email) {
		logger.info("Entered: Method: saveEmail(), Class: MailProcessor");
		emailStoreService.saveEmail(
				email.get(EMAIL_SUBJECT),
				email.get(EMAIL_SENT_DATE),
				email.get(EMAIL_FROM),
				email.get(EMAIL_TO),
				email.get(CONTENT_TYPE),
				email.get(EMAIL_BODY),
				email.get(HAS_ATTACHMENT),
				email.get(ATTACHMENT_COUNT),
				email.get(ATTACHMENT_NAMES)
				);
		logger.info("Exited: Method: saveEmail(), Class: MailProcessor");
	}

	public void printEmail(Map<String, String> email) {
		logger.info("Entered: Method: printEmail(), Class: MailProcessor");
		GenericUtils.coutLn("=============================");
		GenericUtils.coutLn("SUBJECT : " + email.get(EMAIL_SUBJECT));
		GenericUtils.coutLn("FROM : " + email.get(EMAIL_FROM));
		GenericUtils.coutLn("TO : " + email.get(EMAIL_TO));
		GenericUtils.coutLn("DATE : " + email.get(EMAIL_SENT_DATE));
		GenericUtils.coutLn("CONTENT_TYPE : " + email.get(CONTENT_TYPE));
		GenericUtils.coutLn("HAS_ATTACHMENT : " + email.get(HAS_ATTACHMENT));
		GenericUtils.coutLn("ATTACHMENT_COUNT : " + email.get(ATTACHMENT_COUNT));
		GenericUtils.coutLn("ATTACHMENT_NAMES : " + email.get(ATTACHMENT_NAMES));
		GenericUtils.coutLn("BODY : " + email.get(EMAIL_BODY));
		GenericUtils.coutLn("=============================");
		logger.info("Exited: Method: printEmail(), Class: MailProcessor");
	}

	
	public Map<String, String> processMimeMessage(MimeMessage message) throws IOException, MessagingException, javax.mail.MessagingException {
		logger.info("Entered: Method: processMimeMessage(), Class: MailProcessor");
		MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();		
		HashMap<String,String> email = new HashMap<>();
		String emailBody = EMPTY_STRING;
		List<String> attachedFiles = new ArrayList<>();
		boolean hasAttachment = false;
		int totalParts = mimeMultipart.getCount();
		logger.debug("Total BodyParts: {}", totalParts);
		for (int i = 0; i < totalParts; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			logger.debug("BodyPart Content: {}", bodyPart.getContent());
			logger.debug("BodyPart Content Type: {}", bodyPart.getContentType());
			logger.debug("BodyPart Disposition: {}", bodyPart.getDisposition());
			if (bodyPart.isMimeType(MIME_TYPE_TEXT_PLAIN)) {
				logger.debug("MIME Type: {}", MIME_TYPE_TEXT_PLAIN);
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
			} else if (bodyPart.isMimeType(MIME_TYPE_TEXT_HTML)) {
				logger.debug("MIME Type: {}", MIME_TYPE_TEXT_HTML);
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
			} else /* if (bodyPart.getContent() instanceof Multipart) */ {	
				logger.debug("BodyPart Disposition: {}", bodyPart.getDisposition());
				if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())  
						|| Part.INLINE.equalsIgnoreCase(bodyPart.getDisposition())) {
					logger.debug("Is Attachment: {}", true);
					hasAttachment = true;
					String fileName = UUID.randomUUID() + SEPARATOR_KEYWORD + bodyPart.getFileName();
					logger.debug("File Name: {}", fileName);
					attachedFiles.add(fileName);
					((MimeBodyPart) bodyPart).saveFile(System.getProperty("user.dir") + ATTACHMENT_STORAGE_PATH + File.separator + fileName);
				} else {
					logger.debug("Is Attachment: {}", false);
					emailBody = emailBody + getContentFromMimeMultipart((MimeMultipart) bodyPart.getContent());
				}
			}
			logger.debug("Iteration: {}",  (i+1));
		}
		email.put(CONTENT_TYPE, message.getContentType());
		email.put(EMAIL_SUBJECT, message.getSubject());
		email.put(EMAIL_FROM, message.getFrom()[0].toString());
		email.put(EMAIL_SENT_DATE, DateUtil.getFormattedDate(message.getSentDate()));
		email.put(EMAIL_TO, message.getAllRecipients()[0].toString());
		email.put(EMAIL_BODY, emailBody);
		email.put(HAS_ATTACHMENT, Boolean.toString(hasAttachment));
		if(hasAttachment) {
			email.put(ATTACHMENT_COUNT, Integer.toString(attachedFiles.size()));
			email.put(ATTACHMENT_NAMES, attachedFiles.toString());
		} else {
			email.put(ATTACHMENT_COUNT, Integer.toString(0));
			email.put(ATTACHMENT_NAMES, Collections.<String>emptyList().toString());
		}
		logger.info("Exited: Method: processMimeMessage(), Class: MailProcessor");
		return email;
	}

	private String getContentFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException, javax.mail.MessagingException {
		logger.info("Entered: Method: getContentFromMimeMultipart(), Class: MailProcessor");
		String emailBody = EMPTY_STRING;
		int totalParts = mimeMultipart.getCount();
		logger.debug("Total BodyParts: {}", totalParts);
		for (int i = 0; i < totalParts; i++) {
			logger.debug("Iteration: {}",  (i+1));
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType(MIME_TYPE_TEXT_PLAIN)) {
				logger.debug("MIME Type: {}", MIME_TYPE_TEXT_PLAIN);
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType(MIME_TYPE_TEXT_HTML)) {
				logger.debug("MIME Type: {}", MIME_TYPE_TEXT_HTML);
				//Do not delete below two lines, kept for future requirements
				//String html = (String) bodyPart.getContent();
				//emailBody = emailBody + SEPARATOR_NEW_LINE + org.jsoup.Jsoup.parse(html).text();
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
			} else if (bodyPart.getContent() instanceof MimeMultipart){
				logger.debug("MIME Type: {}", MIME_TYPE_MULTIPART);
				emailBody = emailBody + getContentFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		logger.info("Exited: Method: getContentFromMimeMultipart(), Class: MailProcessor");
		return emailBody;
	}
}
