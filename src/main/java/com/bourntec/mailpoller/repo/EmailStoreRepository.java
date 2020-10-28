package com.bourntec.mailpoller.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.bourntec.mailpoller.models.EmailStoreModel;

/**
 * @author Gopal
 *
 */
@Repository
public interface EmailStoreRepository extends JpaRepository<EmailStoreModel, Long> {

	List<EmailStoreModel> findByEmailSubject(String subject);
	EmailStoreModel findByEmailId(Long emailId);



}
