package com.rohitmunde.trustdesk;

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

	@Test
	void contextLoads() {
	}

}
