/**
 * 
 */
package com.bourntec.mailpoller.utils;

/**
 * 
 * @author gopal
 * @since 21-08-2020
 * @description String literals used all over the application are defined here.
 * 
 */
public final class ConstantLiterals {
	private ConstantLiterals() { }

	// General
	public static final String DIGITS_REGULAR_EXPRESSION = "\\d+";
	public static final String SEPARATOR_NEW_LINE = "\n";
	public static final String SEPARATOR_TAB = "\t";
	public static final String SEPARATOR_KEYWORD = "_";
	public static final String EMPTY_STRING = "";
	
	// Email Headers
	public static final String CONTENT_TYPE = "CONTENT_TYPE";
	public static final String EMAIL_SUBJECT = "EMAIL_SUBJECT";
	public static final String EMAIL_FROM = "EMAIL_FROM";
	public static final String EMAIL_SENT_DATE = "EMAIL_SENT_DATE";
	public static final String EMAIL_TO = "EMAIL_TO";
	public static final String EMAIL_BODY = "EMAIL_BODY";
	public static final String ATTACHMENT_COUNT = "ATTACHMENT_COUNT";
	public static final String ATTACHMENT_NAMES = "ATTACHMENT_NAMES";
	public static final String HAS_ATTACHMENT = "HAS_ATTACHMENT";

	// Mime Types
	public static final String MIME_TYPE_MULTIPART_WILDCARD = "multipart/*";
	public static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
	public static final String MIME_TYPE_TEXT_HTML = "text/html";
	public static final String MIME_TYPE_MULTIPART = "multipart";
	
	// Email Properties
	public static final String PROPERTY_MAIL_MIME_ALLOW_UTF_8 = "mail.mime.allowutf8";
	public static final String PROPERTY_MAIL_IMAP_CONNECTION_POOL_SIZE = "mail.imap.connectionpoolsize";
	public static final String PROPERTY_MAIL_IMAP_START_TLS_ENABLE = "mail.imap.starttls.enable";
	public static final String PROPERTY_MAIL_IMAP_SSL_TRUST = "mail.imap.ssl.trust";
	public static final String PROPERTY_MAIL_HOST = "mail.host";
	public static final String PROPERTY_MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	
	// Property Values
	public static final String STRING_VALUE_CONNECTION_POOL_SIZE = "5";
	public static final String STRING_VALUE_WILDCARD = "*";
	public static final String STRING_VALUE_TRUE = "true";
	public static final String STRING_VALUE_FALSE = "false";
	public static final String STRING_VALUE_TRANSPORT_PROTOCOL = "imap";
	
	// Attachment Processing
	public static final String ATTACHMENT_STORAGE_PATH = "/attachments";

}
