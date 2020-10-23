/**
 * 
 */
package com.bourntec.mailpoller.flows.utils;

import static com.bourntec.mailpoller.utils.ConstantLiterals.ATTACHMENT_COUNT;
import static com.bourntec.mailpoller.utils.ConstantLiterals.ATTACHMENT_NAMES;
import static com.bourntec.mailpoller.utils.ConstantLiterals.CONTENT_TYPE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_BODY;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_FROM;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_SENT_DATE;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_SUBJECT;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMAIL_TO;
import static com.bourntec.mailpoller.utils.ConstantLiterals.EMPTY_STRING;
import static com.bourntec.mailpoller.utils.ConstantLiterals.HAS_ATTACHMENT;
import static com.bourntec.mailpoller.utils.ConstantLiterals.MIME_TYPE_TEXT_HTML;
import static com.bourntec.mailpoller.utils.ConstantLiterals.MIME_TYPE_TEXT_PLAIN;
import static com.bourntec.mailpoller.utils.ConstantLiterals.SEPARATOR_NEW_LINE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Queue;
import javax.mail.BodyPart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

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

	@Autowired
	private EmailStoreService emailStoreService;
	
    @Autowired
    JmsTemplate jmsTemplate;
    
    @Autowired
    Queue saveQueue;

    @Autowired
    Queue persistQueue;
    
	public Object logMail(MimeMessage message) throws IOException, javax.mail.MessagingException {
		System.out.println("logMail()");
		HashMap<String,String> email;
		try {
			// GenericUtils.coutLn(message.getContent().toString());
			email = (HashMap<String, String>) processMimeMessage(message);
			
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			message.writeTo(baos);
//			byte[] msgBytes = baos.toByteArray(); 
//			jmsTemplate.convertAndSend(saveQueue, msgBytes);
			
			jmsTemplate.convertAndSend(persistQueue, email);
//			printEmail(email);
//			saveEmail(email);
		} catch(ClassCastException ce) {
			System.out.println("Class cast exception");
			System.out.println(message.getContent().toString());
		}
		catch (Exception e) {
			System.out.println("### Read mail content error");
			e.printStackTrace();
		}
		return null;
	}

	public void saveEmail(HashMap<String, String> email) {
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
	}


	public void printEmail(HashMap<String, String> email) {
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
	}

	
	public Map<String, String> processMimeMessage(MimeMessage message) throws IOException, MessagingException, javax.mail.MessagingException {
		System.out.println("getContentFromMimeMultipart()");
		MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();		
		HashMap<String,String> email = new HashMap<>();
		String emailBody = EMPTY_STRING;
		List<String> attachedFiles = new ArrayList<>();
		boolean hasAttachment = false;
		int totalParts = mimeMultipart.getCount();
		System.out.println("TOTAL PARTS: " + totalParts);
		for (int i = 0; i < totalParts; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			System.out.println(bodyPart.getContent());
			System.out.println(bodyPart.getContentType());
			System.out.println(bodyPart.getDisposition());
			if (bodyPart.isMimeType(MIME_TYPE_TEXT_PLAIN)) {
				System.out.println(MIME_TYPE_TEXT_PLAIN);
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
			} else if (bodyPart.isMimeType(MIME_TYPE_TEXT_HTML)) {
				System.out.println(MIME_TYPE_TEXT_HTML);
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
			} else /* if (bodyPart.getContent() instanceof Multipart) */ {	
				System.out.println("DISPOSITION: " + bodyPart.getDisposition());
				if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())  
						|| Part.INLINE.equalsIgnoreCase(bodyPart.getDisposition())) {
					hasAttachment = true;
					String fileName = bodyPart.getFileName();
					System.out.println("FILE");
					attachedFiles.add(fileName);
					((MimeBodyPart) bodyPart).saveFile(System.getProperty("user.dir") + "/attachments" + File.separator + fileName);
				} else {
					System.out.println("TEXT");
					emailBody = emailBody + getContentFromMimeMultipart((MimeMultipart) bodyPart.getContent());
				}
			}
			System.out.println("Iteration: " +  (i+1));
		}
		System.out.println(emailBody);
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
		return email;
	}

	private String getContentFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException, javax.mail.MessagingException {
		String emailBody = EMPTY_STRING;
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType(MIME_TYPE_TEXT_PLAIN)) {
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} else if (bodyPart.isMimeType(MIME_TYPE_TEXT_HTML)) {
				//Do not delete below two lines, kept for future requirements
				//String html = (String) bodyPart.getContent();
				//emailBody = emailBody + SEPARATOR_NEW_LINE + org.jsoup.Jsoup.parse(html).text();
				emailBody = emailBody + SEPARATOR_NEW_LINE + bodyPart.getContent();
			} else if (bodyPart.getContent() instanceof MimeMultipart){
				emailBody = emailBody + getContentFromMimeMultipart((MimeMultipart) bodyPart.getContent());
			}
		}
		return emailBody;
	}
}
