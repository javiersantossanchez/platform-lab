package com.platform.general.microservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.platform.general.microservice.web.credential.WebCredential;
import com.platform.general.microservice.web.credential.adapters.web.ErrorResponse;
import com.platform.general.microservice.web.credential.adapters.web.dtos.WebCredentialParam;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class ApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

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
		).andExpect(status().is(400));
	}


	//@Test
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
							)
							.andExpect(status().isOk())
							.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		WebCredentialParam[] pojos = objectMapper.readValue(response, WebCredentialParam[].class);

		Assertions.assertEquals(2,pojos.length);
		for(WebCredentialParam current : pojos){
			Assertions.assertTrue(current.equals(body1) || current.equals(body2));
		}
	}

	//@Test
	void insertMultiplesCredentialsAndFindOneById() throws Exception {
		WebCredentialParam body1 = new WebCredentialParam();
		body1.setPassword("asdQSASAed1");
		body1.setUserName(faker.name().username());
		body1.setWebSite(faker.internet().domainName());

		MvcResult credential1 = mockMvc.perform(
				post("/web-credentials")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(body1))
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
		).andExpect(status().isOk()).andReturn();



		MvcResult mvcResult = mockMvc.perform(
						get("/web-credentials/"+pojo1.getId())
								.contentType(MediaType.APPLICATION_JSON)
				)
				.andExpect(status().isOk())
				.andReturn();
		String response = mvcResult.getResponse().getContentAsString();
		WebCredentialParam pojoResponse = objectMapper.readValue(response, WebCredentialParam.class);

		Assertions.assertEquals(pojo1.getPassword(),pojoResponse.getPassword());
		Assertions.assertEquals(pojo1.getUserName(),pojoResponse.getUserName());
		Assertions.assertEquals(pojo1.getWebSite(),pojoResponse.getWebSite());
	}

	//@Test´`+
	void contextLoads() {
	}

}
