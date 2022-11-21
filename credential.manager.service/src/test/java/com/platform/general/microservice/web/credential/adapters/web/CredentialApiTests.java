package com.platform.general.microservice.web.credential.adapters.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialDao;
import com.platform.general.microservice.web.credential.adapters.postgresql.WebCredentialEntity;
import com.platform.general.microservice.web.credential.adapters.web.dtos.WebCredentialParam;
import com.platform.general.microservice.web.credential.adapters.web.error.ErrorResponse;
import com.platform.general.microservice.web.credential.exceptions.IllegalArgumentException;
import com.platform.general.microservice.web.credential.exceptions.WebCredentialNotFoundException;
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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ImportAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class})
@Testcontainers
class CredentialApiTests {

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
		WebCredentialNotFoundException expectedResponse = new WebCredentialNotFoundException();

		MvcResult mvcResult = mockMvc.perform(
				get("/web-credentials/"+ UUID.randomUUID())
						.contentType(MediaType.APPLICATION_JSON)
						.with(jwt())
		).andExpect(status().is4xxClientError()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);
		Assertions.assertEquals(expectedResponse.getErrorMessage(), error.getErrorMessage());
	}

	@Test
	void searchCredentialWithInvalidId() throws Exception {

		MvcResult mvcResult = mockMvc.perform(
				get("/web-credentials/invalid-UUID")
						.contentType(MediaType.APPLICATION_JSON)
						.with(jwt())
		).andExpect(status().is4xxClientError()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);
		Assertions.assertTrue(error.getErrorMessage().startsWith("Invalid value for "));
	}

	@Test
	void searchCredentialWhenOk() throws Exception {
		WebCredentialEntity entity = WebCredentialEntity.builder()
										.credentialName(faker.company().name())
										.userName(faker.name().username())
										.password(faker.internet().password())
										.creationTime(LocalDateTime.now())
										.build();
		entity = dao.save(entity);

		MvcResult mvcResult = mockMvc.perform(
				get("/web-credentials/"+entity.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.with(jwt())
		).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		WebCredential credential = objectMapper.readValue(response, WebCredential.class);
		Assertions.assertEquals(entity.getUserName(),credential.getUserName());
		Assertions.assertEquals(entity.getUserName(),credential.getUserName());
		Assertions.assertEquals(entity.getCreationTime(),credential.getCreationDate());
		Assertions.assertEquals(entity.getId(),credential.getId());
	}
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////////

	@ParameterizedTest
	@NullSource
	@EmptySource
	void createCredentialWhenEmptyPassword(String password) throws Exception {
		WebCredentialParam body = new WebCredentialParam();
		body.setPassword(password);
		body.setUserName(faker.name().username());
		body.setCredentialName(faker.internet().domainName());

		Jwt jwt = Jwt.withTokenValue("token")
				.header("alg", "none")
				.claim("sub", "f2411d84-19a9-4f24-89e0-68aab1490e99")
				.claim("scope", "openid profile email")
				.claim("sid", "0244e8ef-c894-40b7-b71a-75ef58ddf533")
				.claim("given_name", "javier")
				.claim("family_name", "santos")
				.build();


		mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body))
						.with(jwt().jwt(jwt))
		).andExpect(status().is(400));
	}

	@ParameterizedTest
	@NullSource
	@EmptySource
	void createCredentialWhenEmptyUserName(String userName) throws Exception {
		WebCredentialParam body = new WebCredentialParam();
		body.setPassword("asdQSASAed2");
		body.setUserName(userName);
		body.setCredentialName(faker.internet().domainName());

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
		body.setCredentialName(webSite);

		mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body))
						.with(jwt())
		).andExpect(status().is(400));
	}

	@Test
	void createCredentialWithUserNameDuplicated() throws Exception {
		IllegalArgumentException exceptionExpected =
				new IllegalArgumentException(IllegalArgumentException.Argument.USER_NAME, IllegalArgumentException.Validation.DUPLICATED);

		String userName = faker.name().username();

		WebCredentialEntity entity = WebCredentialEntity.builder()
				.credentialName(faker.company().name())
				.userName(userName)
				.password(faker.internet().password())
				.creationTime(LocalDateTime.now())
				.build();
		dao.save(entity);


		WebCredentialParam credential2 = new WebCredentialParam();
		credential2.setPassword("asdQSASAed1");
		credential2.setUserName(userName);
		credential2.setCredentialName(faker.internet().domainName());

		MvcResult mvcResult = mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(credential2))
						.with(jwt())
		).andExpect(status().is4xxClientError()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);
		Assertions.assertEquals(exceptionExpected.getErrorMessage(),error.getErrorMessage());
	}

	@Test
	void createCredentialWhenOK() throws Exception {

		WebCredentialParam credential2 = new WebCredentialParam();
		credential2.setPassword("asdQSASAed1");
		credential2.setUserName(faker.name().username());
		credential2.setCredentialName(faker.internet().domainName());

		MvcResult mvcResult = mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(credential2))
						.with(jwt())
		).andExpect(status().isOk()).andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		WebCredential newCredential = objectMapper.readValue(response, WebCredential.class);
		Assertions.assertEquals(credential2.getUserName(),newCredential.getUserName());
		Assertions.assertEquals(credential2.getPassword(),newCredential.getPassword());
		Assertions.assertEquals(credential2.getCredentialName(),newCredential.getCredentialName());
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
		body1.setCredentialName(faker.internet().domainName());

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
		body2.setCredentialName(faker.internet().domainName());

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
		Assertions.assertEquals(pojo1.getCredentialName(),pojoResponse.getCredentialName());
	}

}
