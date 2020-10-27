package com.bourntec.mailpoller.alt;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessagingException;

import com.bourntec.mailpoller.utils.DateUtil;

public class GmailFetch {
	private static Logger logger = LoggerFactory.getLogger(GmailFetch.class);


	//use with schedule frameworks
	public static void main(String[] args) throws Exception {

		Session session = Session.getDefaultInstance(new Properties());
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", 993, "saturnkings@gmail.com", "inboxbygmail");
		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_WRITE);

		Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

		System.out.println("messages " + messages.length);
		int counter = 1;

		for (Message message : messages) {
			message.setFlag(Flags.Flag.SEEN, true);
			message.writeTo(new FileOutputStream(new File("d:/temp/"+ message.getMessageNumber() + ".eml")));
			logger.info("sendDate: {} ,subject {}" ,message.getSentDate(), message.getSubject());
			if(counter > 20) break; else counter++;
		}

		inbox.close();
		store.close();

		Properties props = System.getProperties();  
		props.put("mail.host", "smtp.dummydomain.com");  
		props.put("mail.transport.protocol", "smtp");  

		File directoryPath = new File("D:\\temp");
		//List of all files and directories
		File filesList[] = directoryPath.listFiles();
		System.out.println("List of files and directories in the specified directory:");
		for(File file : filesList) {
			System.out.println("File name: "+file.getName());
			System.out.println("File path: "+file.getAbsolutePath());
			System.out.println("Size :"+file.getTotalSpace());
			System.out.println(" ");

if(!file.isDirectory()) {
			Session mailSession = Session.getDefaultInstance(props, null);  
			InputStream source = new FileInputStream(file);  
			MimeMessage message = new MimeMessage(mailSession, source);  
			processMimeMessage(message).toString();


		}
}
	}

	public static Map<String, String> processMimeMessage(MimeMessage message) throws IOException, MessagingException, javax.mail.MessagingException {
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
					((MimeBodyPart) bodyPart).saveFile( "d:/temp/pfa" + File.separator + fileName);
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

	private static String getContentFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException, javax.mail.MessagingException {
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
