/**
 * 
 */
package com.bourntec.mailpoller.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Gopal
 *
 */
@Entity
@Table(name="email_store")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailStoreModel  implements Serializable {
	
	private static final long serialVersionUID = -8746632356233658107L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long emailId;
	
	
	@Column(nullable = false, updatable = true)
	private String emailSubject;
	
	@NotBlank
	@Column(nullable = false, updatable = true)
	private String emailFrom;
	
	@NotBlank
	@Column(nullable = false, updatable = true)
	private String emailTo;
	
	@NotBlank
	@Column(nullable = false, updatable = true)
	private String emailContentType;
	
	@NotBlank
	@Column(nullable = false, updatable = true)
	private String emailSentDate;
	
	@Lob
	@Column(nullable = false, updatable = true)
	private String emailBody;
	
	@Column(nullable = false, updatable = true)
	private Boolean emailHasAttachment;
	
	@Column(nullable = false, updatable = true)
	private Integer emailAttachmentCount;
	
	@NotBlank
	@Column(nullable = false, updatable = true, columnDefinition = "varchar(4096)")
	private String emailAttachmentNames;

	public Long getEmailId() {
		return emailId;
	}

	public void setEmailId(Long emailId) {
		this.emailId = emailId;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailContentType() {
		return emailContentType;
	}

	public void setEmailContentType(String emailContentType) {
		this.emailContentType = emailContentType;
	}

	public String getEmailSentDate() {
		return emailSentDate;
	}

	public void setEmailSentDate(String emailSentDate) {
		this.emailSentDate = emailSentDate;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public Boolean getEmailHasAttachment() {
		return emailHasAttachment;
	}

	public void setEmailHasAttachment(Boolean emailHasAttachment) {
		this.emailHasAttachment = emailHasAttachment;
	}

	public Integer getEmailAttachmentCount() {
		return emailAttachmentCount;
	}

	public void setEmailAttachmentCount(Integer emailAttachmentCount) {
		this.emailAttachmentCount = emailAttachmentCount;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getEmailAttachmentNames() {
		return emailAttachmentNames;
	}

	public void setEmailAttachmentNames(String emailAttachmentNames) {
		this.emailAttachmentNames = emailAttachmentNames;
	}

	@Override
	public String toString() {
		return "EmailStoreModel [emailId=" + emailId + ", emailSubject=" + emailSubject + ", emailFrom=" + emailFrom
				+ ", emailTo=" + emailTo + ", emailContentType=" + emailContentType + ", emailSentDate=" + emailSentDate
				+ ", emailBody=" + emailBody + ", emailHasAttachment=" + emailHasAttachment + ", emailAttachmentCount="
				+ emailAttachmentCount + ", emailAttachmentNames=" + emailAttachmentNames + "]";
	}



	
	
	
}
