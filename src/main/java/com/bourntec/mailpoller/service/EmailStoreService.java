package com.bourntec.mailpoller.service;


import java.util.List;
import org.springframework.stereotype.Service;

import com.bourntec.mailpoller.models.EmailStoreModel;

/**
 * @author Gopal
 *
 */
@Service(value = "emailStoreService")
public interface EmailStoreService {

	EmailStoreModel findByEmailId(Long emailId);
	List<EmailStoreModel> findBySubject(String subject);
	public EmailStoreModel saveEmail(String subject, String sentDate, String from, String to, String contentType, String body,
			String hasAttachment, String attachmentCount, String attachmentNames);

}
