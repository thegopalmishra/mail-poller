package com.bourntec.mailpoller.flows;

import org.springframework.integration.dsl.IntegrationFlow;

public interface ImapIntegrationFlow {

	public IntegrationFlow integrationFlow(String email, String password);

}