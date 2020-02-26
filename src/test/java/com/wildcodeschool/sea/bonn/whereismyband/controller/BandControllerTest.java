package com.wildcodeschool.sea.bonn.whereismyband.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;

@SpringBootTest
@Transactional
@WithUserDetails("elke@e-gitarre.de")
//set property file to be used here (to be saved in /src/test/resources)
@TestPropertySource("classpath:/h2-application.properties")
class BandControllerTest {

	MockMvc mock;

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private BandRepository bandRepository;

	@BeforeEach
	void setup() {
		mock = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build();
	}

	@Test
	void shouldShowProfileWithGivenId() throws Exception {

		// Given | Arrange
		Band band = new Band();
		band.setId(1L);

		// When | Act
		MvcResult result = mock.perform(MockMvcRequestBuilders.get("/band/" + band.getId() + "/view")).andReturn();

		// Then | Assert
		assertThat(result.getResponse().getStatus()).isEqualTo(200);
		ModelMap attributeMap = result.getModelAndView().getModelMap();
		band = (Band) attributeMap.get("band");
		assertThat(band.getName()).isEqualTo("ACDC");

	}

	@Test
	void shouldEditBandProfile() throws Exception {
		
		//Given | Arrange
		Band band = bandRepository.findByName("ACDC").get();
		
		band.setName("BCDC");
		
		//When | Act
		MvcResult result = mock
				.perform(MockMvcRequestBuilders
						.post("/band/" + band.getId() + "/edit")
				.with(SecurityMockMvcRequestPostProcessors.csrf()) 
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.flashAttr("band", band)).andReturn();
				
		//Then | Assert
		assertThat(result.getResponse().getStatus()).isEqualTo(302);
		
		Optional<Band> bandO = bandRepository.findByName("BCDC");
		assertThat(bandO.isPresent()).isTrue();
		bandO = bandRepository.findByName("ACDC");
		assertThat(bandO.isEmpty()).isTrue();
	}
}
