package com.bourntec.mailpoller;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.mail.Flags;
import javax.mail.internet.MimeMessage;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.context.StandardIntegrationFlowContext;
import org.springframework.integration.mail.support.DefaultMailHeaderMapper;
import org.springframework.integration.mapping.HeaderMapper;
import org.springframework.integration.scheduling.PollerMetadata;

import com.bourntec.mailpoller.beans.EmailConfig;
import com.bourntec.mailpoller.flows.ImapIntegrationFlow;
import com.bourntec.mailpoller.utils.PrintUtil;
import com.bourntec.mailpoller.utils.PrintUtil.Colors;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootApplication
@ComponentScan(basePackages = {"com.bourntec.mailpoller.*"})
public class MailPollerApplication {

	public static void main(String[] args) {
		printModifier();
		SpringApplication.run(MailPollerApplication.class, args);
	}

	private ObjectMapper mapper = new ObjectMapper();

	@Value("${poller.emails}")
	private String emails;


	@Autowired
	private ApplicationContext appContext;


	@Autowired
	private StandardIntegrationFlowContext context;

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		System.out.println("poller()");
		return Pollers.fixedRate(1000).maxMessagesPerPoll(100).get();

	}

	@PostConstruct
	private void setupPollers() throws IOException {
		System.out.println("setupPollers()");
		List<EmailConfig> emailConfigs = mapper.readValue(emails, new TypeReference<List<EmailConfig>>() {});
		for (EmailConfig item : emailConfigs) {
			ImapIntegrationFlow flow = appContext.getBean(item.getFetchStrategy(),ImapIntegrationFlow.class);
			context.registration(flow.integrationFlow(item.getEmail(),item.getPassword())).register();
		}

	}


	@Bean("mailHeaderMapper")
	public HeaderMapper<MimeMessage> mailHeaderMapper() {
		System.out.println("mailHeaderMapper()");
		return new DefaultMailHeaderMapper();
	}


	@Bean("imapURL")
	public String imapUrl() {
		System.out.println("imapUrl()");
//		return "imaps://imap.gmail.com:993/inbox";
		return "imaps://outlook.office365.com:993/inbox";
	}


	@Bean("searchTerm")
	public SearchTerm notSeenTerm() {
		System.out.println("notSeenTerm()");
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
