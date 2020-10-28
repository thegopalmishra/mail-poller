package com.bourntec.mailpoller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.Flags;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.context.StandardIntegrationFlowContext;
import org.springframework.integration.scheduling.PollerMetadata;

import com.bourntec.mailpoller.beans.EmailConfig;
import com.bourntec.mailpoller.flows.ImapIntegrationFlow;
import com.bourntec.mailpoller.utils.PrintUtil;
import com.bourntec.mailpoller.utils.PrintUtil.Colors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Gopal
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.bourntec.mailpoller.*"})
public class MailPollerApplication {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	public static void main(String[] args) {
		// Uncomment when debugging - Prints all sout(s) in Green.
		printModifier();
		SpringApplication.run(MailPollerApplication.class, args);
	}

	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Read emails to poll from application.yml
	 */
	@Value("${poller.emails}")
	private String emails;

	/**
	 * Read imap server url from application.yml
	 */
	@Value("${poller.server}")
	private String server;


	@Autowired
	private ApplicationContext appContext;


	@Autowired
	private StandardIntegrationFlowContext context;

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		logger.info("Entered & Exited: Method: poller(), Class: MailPollerApplication");
		return Pollers.fixedRate(1000).maxMessagesPerPoll(100).get();

	}

	@PostConstruct
	private void setupPollers() throws IOException {
		logger.info("Entered: Method: setupPollers(), Class: MailPollerApplication");
		List<EmailConfig> emailConfigs = mapper.readValue(emails, new TypeReference<List<EmailConfig>>() {});

		for (EmailConfig item : emailConfigs) {
			ImapIntegrationFlow flow = appContext.getBean(item.getFetchStrategy(),ImapIntegrationFlow.class);
			context.registration(flow.integrationFlow(item.getEmail(),item.getPassword())).register();
		}
		logger.info("Exited: Method: setupPollers(), Class: MailPollerApplication");

	}


	/**
	 * IMAP URL configured in application.yml
	 * @return String
	 */
	@Bean("imapURL")
	public String imapUrl() {
		logger.info("Entered & Exited: Method: imapUrl(), Class: MailPollerApplication");
		return server;
	}


	/**
	 * USed to configure search term used by integration flows
	 * @return SearchTerm
	 */
	@Bean("searchTerm")
	public SearchTerm notSeenTerm() {
		logger.info("Entered & Exited: Method: notSeenTerm(), Class: MailPollerApplication");
		return new FlagTerm(new Flags(Flags.Flag.SEEN), false);
	}

	/**
	 * Overrides the default Java print stream to append line number and a link to it. 
	 * 
	 * <p>Also provides method to format the console output.</p>
	 *  
	 *  @since 2020-07-23
	 *  @author gopal
	 */
	static void printModifier() {
		PrintUtil.setProperties(Colors.GREEN, Colors.NONE, false, false, true, false, true);
		PrintUtil.printCustomizedOutputWithLineNumbers();
	}


}
