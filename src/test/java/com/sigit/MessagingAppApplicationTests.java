package com.sigit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sigit.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = MessagingAppApplication.class)
@AutoConfigureMockMvc
public class MessagingAppApplicationTests {

	private static final Message m1 = new Message("Adi", "Budi", "Apa kabar Bud?");
	private static final Message m2 = new Message("Budi", "Adi", "Baik. Apa kabar juga Ad?");
	private static final Message m3 = new Message("Adi", "Budi", "Haha.. Baik juga");

	@Autowired
	MockMvc mockMvc;

	@Test
	public void contextLoads() throws Exception {

		//send message 1
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson=ow.writeValueAsString(m1);

		mockMvc.perform(post("/api/send").contentType(APPLICATION_JSON_UTF8_VALUE )
				.content(requestJson))
				.andDo(print())
				.andExpect(jsonPath("$.sender", containsString(m1.getSender())))
				.andExpect(jsonPath("$.receiver", containsString(m1.getReceiver())))
				.andExpect(jsonPath("$.message", containsString(m1.getMessage())))
				.andExpect(status().isOk());

		//send message 2
		requestJson=ow.writeValueAsString(m2);

		mockMvc.perform(post("/api/send").contentType(APPLICATION_JSON_UTF8_VALUE )
				.content(requestJson))
				.andDo(print())
				.andExpect(jsonPath("$.sender", containsString(m2.getSender())))
				.andExpect(jsonPath("$.receiver", containsString(m2.getReceiver())))
				.andExpect(jsonPath("$.message", containsString(m2.getMessage())))
				.andExpect(status().isOk());

		//send message 3
		requestJson=ow.writeValueAsString(m3);

		mockMvc.perform(post("/api/send").contentType(APPLICATION_JSON_UTF8_VALUE )
				.content(requestJson))
				.andDo(print())
				.andExpect(jsonPath("$.sender", containsString(m3.getSender())))
				.andExpect(jsonPath("$.receiver", containsString(m3.getReceiver())))
				.andExpect(jsonPath("$.message", containsString(m3.getMessage())))
				.andExpect(status().isOk());

		//get message from Adi
		mockMvc.perform(get("/api/get/message/Adi").contentType(APPLICATION_JSON_UTF8_VALUE))
				.andDo(print())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$.[0].sender", containsString(m2.getSender())))
				.andExpect(jsonPath("$.[0].receiver", containsString(m2.getReceiver())))
				.andExpect(jsonPath("$.[0].message", containsString(m2.getMessage())))
				.andExpect(status().isOk());

		//get message from Budi
		mockMvc.perform(get("/api/get/message/Budi").contentType(APPLICATION_JSON_UTF8_VALUE))
				.andDo(print())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$.[0].sender", containsString(m1.getSender())))
				.andExpect(jsonPath("$.[0].receiver", containsString(m1.getReceiver())))
				.andExpect(jsonPath("$.[0].message", containsString(m1.getMessage())))
				.andExpect(jsonPath("$.[1].sender", containsString(m3.getSender())))
				.andExpect(jsonPath("$.[1].receiver", containsString(m3.getReceiver())))
				.andExpect(jsonPath("$.[1].message", containsString(m3.getMessage())))
				.andExpect(status().isOk());

		//get realtime message from Adi's side
		mockMvc.perform(get("/api/get/realtime/Adi/Budi").contentType(APPLICATION_JSON_UTF8_VALUE))
				.andDo(print())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$.[0].sender", containsString(m1.getSender())))
				.andExpect(jsonPath("$.[0].receiver", containsString(m1.getReceiver())))
				.andExpect(jsonPath("$.[0].message", containsString(m1.getMessage())))
				.andExpect(jsonPath("$.[1].sender", containsString(m2.getSender())))
				.andExpect(jsonPath("$.[1].receiver", containsString(m2.getReceiver())))
				.andExpect(jsonPath("$.[1].message", containsString(m2.getMessage())))
				.andExpect(jsonPath("$.[2].sender", containsString(m3.getSender())))
				.andExpect(jsonPath("$.[2].receiver", containsString(m3.getReceiver())))
				.andExpect(jsonPath("$.[2].message", containsString(m3.getMessage())))
				.andExpect(status().isOk());

	}

}

