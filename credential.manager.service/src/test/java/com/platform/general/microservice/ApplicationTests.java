package com.platform.general.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.web.dtos.WebCredentialParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
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


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
@Testcontainers
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	@SuppressWarnings("unused")
	private JwtDecoder jwtDecoder;

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

	@ParameterizedTest
	@NullSource
	@EmptySource
	void createCredentialWhenEmptyPassword(String password) throws Exception {
		WebCredentialParam body = new WebCredentialParam();
		body.setPassword(password);
		body.setUserName(faker.name().username());
		body.setWebSite(faker.internet().domainName());

		mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body))
						.with(jwt())
		).andExpect(status().is(400));
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	void createCredentialWhenEmptyUserName(String userName) throws Exception {
		WebCredentialParam body = new WebCredentialParam();
		body.setPassword("asdQSASAed2");
		body.setUserName(userName);
		body.setWebSite(faker.internet().domainName());

		mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body))
						.with(jwt())
		).andExpect(status().is(400));
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	void createCredentialWhenEmptyWebSite(String webSite) throws Exception {
		WebCredentialParam body = new WebCredentialParam();
		body.setPassword("asdQSASAed2");
		body.setUserName(faker.name().username());
		body.setWebSite(webSite);

		mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body))
						.with(jwt())
		).andExpect(status().is(400));
	}

/**
	@Test
	void insertMultiplesCredentialsAndSearchAll() throws Exception {
		WebCredentialParam body1 = new WebCredentialParam();
		body1.setPassword("asdQSASAed1");
		body1.setUserName(faker.name().username());
		body1.setWebSite(faker.internet().domainName());

		mockMvc.perform(
			post("/web-credentials")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(body1))
		).andExpect(status().isOk());


		WebCredentialParam body2 = new WebCredentialParam();
		body2.setPassword("asdQSASAed2");
		body2.setUserName(faker.name().username());
		body2.setWebSite(faker.internet().domainName());

		mockMvc.perform(
			post("/web-credentials")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsBytes(body2))
		).andExpect(status().isOk());


		MvcResult mvcResult = mockMvc.perform(
								get("/web-credentials")
								.contentType(MediaType.APPLICATION_JSON)
							).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		WebCredential[] pojos = objectMapper.readValue(response, WebCredential[].class);

		Assertions.assertTrue(
				Arrays.stream(pojos).anyMatch(current ->
						current.getPassword().equals(body1.getPassword())&&current.getUserName().equals(body1.getUserName())
								&& current.getWebSite().equals(body1.getWebSite())
				));
		Assertions.assertTrue(
				Arrays.stream(pojos).anyMatch(current ->
						current.getPassword().equals(body2.getPassword())&&current.getUserName().equals(body2.getUserName())
								&& current.getWebSite().equals(body2.getWebSite())
				));
	}

	@Test
	void insertMultiplesCredentialsThenDeleteOneAndSearchAll() throws Exception {
		WebCredentialParam body1 = new WebCredentialParam();
		body1.setPassword("asdQSASAed1");
		body1.setUserName(faker.name().username());
		body1.setWebSite(faker.internet().domainName());
		mockMvc.perform(
				post("/web-credentials")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(body1))
		).andExpect(status().isOk());


		WebCredentialParam body2 = new WebCredentialParam();
		body2.setPassword("asdQSASAed2");
		body2.setUserName(faker.name().username());
		body2.setWebSite(faker.internet().domainName());
		MvcResult credential2 = mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body2))
		).andExpect(status().isOk()).andReturn();
		String response1 = credential2.getResponse().getContentAsString();
		WebCredential pojo2 = objectMapper.readValue(response1, WebCredential.class);


		WebCredentialParam body3 = new WebCredentialParam();
		body3.setPassword("asdQSASAed2");
		body3.setUserName(faker.name().username());
		body3.setWebSite(faker.internet().domainName());
		mockMvc.perform(
				post("/web-credentials")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(body3))
		).andExpect(status().isOk());


		mockMvc.perform(
				delete("/web-credentials/"+pojo2.getId())
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk());


		MvcResult mvcResult = mockMvc.perform(
				get("/web-credentials")
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		WebCredential[] pojos = objectMapper.readValue(response, WebCredential[].class);

		Assertions.assertTrue(
		Arrays.stream(pojos).anyMatch(current ->
			current.getPassword().equals(body1.getPassword())&&current.getUserName().equals(body1.getUserName())
			&& current.getWebSite().equals(body1.getWebSite())
		));
		Assertions.assertTrue(
		Arrays.stream(pojos).anyMatch(current ->
				current.getPassword().equals(body3.getPassword())&&current.getUserName().equals(body3.getUserName())
						&& current.getWebSite().equals(body3.getWebSite())
		));
	}
**/
	@Test
	void insertMultiplesCredentialsAndFindOneById() throws Exception {
		WebCredentialParam body1 = new WebCredentialParam();
		body1.setPassword("asdQSASAed1");
		body1.setUserName(faker.name().username());
		body1.setWebSite(faker.internet().domainName());

		MvcResult credential1 = mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body1))
						.with(jwt())
		).andExpect(status().isOk()).andReturn();
		String response1 = credential1.getResponse().getContentAsString();
		WebCredential pojo1 = objectMapper.readValue(response1, WebCredential.class);


		WebCredentialParam body2 = new WebCredentialParam();
		body2.setPassword("asdQSASAed2");
		body2.setUserName(faker.name().username());
		body2.setWebSite(faker.internet().domainName());

		MvcResult credential2 = mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body2))
						.with(jwt())
		).andExpect(status().isOk()).andReturn();



		MvcResult mvcResult = mockMvc.perform(
						get("/web-credentials/"+pojo1.getId())
								.contentType(MediaType.APPLICATION_JSON)
								.with(jwt())
				)
				.andExpect(status().isOk())
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		WebCredentialParam pojoResponse = objectMapper.readValue(response, WebCredentialParam.class);

		Assertions.assertEquals(pojo1.getPassword(),pojoResponse.getPassword());
		Assertions.assertEquals(pojo1.getUserName(),pojoResponse.getUserName());
		Assertions.assertEquals(pojo1.getWebSite(),pojoResponse.getWebSite());
	}

}
