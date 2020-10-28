/**
 * 
 */
package com.bourntec.mailpoller.service.impl;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bourntec.mailpoller.models.EmailStoreModel;
import com.bourntec.mailpoller.repo.EmailStoreRepository;
import com.bourntec.mailpoller.service.EmailStoreService;


/**
 * @author Gopal
 *
 */
@Service(value = "emailStoreService")
public class EmailStoreServiceImpl implements EmailStoreService {

	@Autowired
	EmailStoreRepository emailStoreRepo;

	private static final Logger logger = LoggerFactory.getLogger(EmailStoreServiceImpl.class);
  

	@Override
	public EmailStoreModel saveEmail(String subject, String sentDate, String from, String to, String contentType, String body,
			String hasAttachment, String attachmentCount, String attachmentNames) {
		logger.info("Entered: Method: saveEmail(), Class: EmailStoreServiceImpl");
		EmailStoreModel esm = new EmailStoreModel();
		esm.setEmailSubject(subject);
		esm.setEmailFrom(from);
		esm.setEmailTo(to);
		esm.setEmailSentDate(sentDate);
		esm.setEmailContentType(contentType);
		esm.setEmailBody(body);
		esm.setEmailHasAttachment(Boolean.parseBoolean(hasAttachment));
		esm.setEmailAttachmentCount(Integer.parseInt(attachmentCount));
		esm.setEmailAttachmentNames(attachmentNames);
		logger.info("Exited: Method: saveEmail(), Class: EmailStoreServiceImpl");
		return emailStoreRepo.save(esm);
	}



	@Override
	public EmailStoreModel findByEmailId(Long emailId) {
		logger.info("Entered & Exited: Method: findByEmailId(), Class: EmailStoreServiceImpl");
		return emailStoreRepo.findById(emailId).get();
	}



	@Override
	public List<EmailStoreModel> findBySubject(String subject) {
		logger.info("Entered & Exited: Method: findBySubject(), Class: EmailStoreServiceImpl");
		return emailStoreRepo.findByEmailSubject(subject);
	}

	
	
}
