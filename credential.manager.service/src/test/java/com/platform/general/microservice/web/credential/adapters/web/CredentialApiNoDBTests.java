package com.platform.general.microservice.web.credential.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialDao;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialEntity;
import com.platform.general.microservice.web.credential.adapters.web.dtos.WebCredentialParam;
import com.platform.general.microservice.web.credential.adapters.web.error.ErrorResponse;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialSearchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
@Testcontainers
class CredentialApiNoDBTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	@SuppressWarnings("unused")
	private JwtDecoder jwtDecoder;

	@Autowired
	private WebCredentialDao dao;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

		registry.add("spring.datasource.url", postgreSQLDBContainer::getJdbcUrl);
		registry.add("spring.datasource.username", postgreSQLDBContainer::getUsername);
		registry.add("spring.datasource.password", postgreSQLDBContainer::getPassword);
		registry.add("spring.datasource.driver-class-name", postgreSQLDBContainer::getDriverClassName);
	}

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.2");

	@Container
	static PostgreSQLContainer postgreSQLDBContainer = new PostgreSQLContainer("postgres:14.5");

	private final Faker faker = new Faker();


	@Test
	void searchCredentialWhenDoesNotExist() throws Exception {
		WebCredentialSearchException expectedResponse = new WebCredentialSearchException();

		postgreSQLDBContainer.stop();
		MvcResult mvcResult = mockMvc.perform(
				get("/web-credentials/"+ UUID.randomUUID())
						.contentType(MediaType.APPLICATION_JSON)
						.with(jwt())
		).andExpect(status().is5xxServerError()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);
		Assertions.assertEquals(expectedResponse.getErrorMessage(), error.getErrorMessage());
	}



}
