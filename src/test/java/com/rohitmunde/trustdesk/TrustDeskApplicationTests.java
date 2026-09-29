package com.rohitmunde.trustdesk;

import com.rohitmunde.trustdesk.repository.AiOperationTraceRepository;
import com.rohitmunde.trustdesk.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration,org.springframework.boot.flyway.autoconfigure.FlywayAutoConfiguration,org.springframework.boot.hibernate.autoconfigure.HibernateJpaAutoConfiguration",
		"spring.security.user.name=test-user",
		"spring.security.user.password=test-password"
})
class TrustDeskApplicationTests {

	@MockitoBean
	private TicketRepository ticketRepository;

	@MockitoBean
	private AiOperationTraceRepository aiOperationTraceRepository;

	@Test
	void contextLoads() {
	}

}
