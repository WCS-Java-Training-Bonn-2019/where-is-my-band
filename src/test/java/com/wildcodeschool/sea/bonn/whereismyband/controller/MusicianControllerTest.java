package com.wildcodeschool.sea.bonn.whereismyband.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.WebApplicationContext;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.EditForm;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@SpringBootTest
@Transactional
@WithUserDetails("elke@e-gitarre.de")
//set property file to be used here (to be saved in /src/test/resources)
@TestPropertySource("classpath:/h2-application.properties")
class MusicianControllerTest {

	MockMvc mock;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private MusicianRepository musicianRepository;

	@BeforeEach
	void setup() {

		mock = MockMvcBuilders
				.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	void shouldShowElkesProfile() throws Exception {

		//Given | Arrange

		//When | Act
		MvcResult result = mock.perform(MockMvcRequestBuilders.get("/musician/view")).andReturn();

		//Then | Assert

		assertThat(result.getResponse().getStatus()).isEqualTo(200);

		ModelMap attributeMap = result.getModelAndView().getModelMap();
		Musician musician = (Musician) attributeMap.get("musician");
		assertThat(musician.getFirstName()).isEqualTo("Elke");

	}


	@Test
	void shouldShowElkesProfileInEditForm() throws Exception {

		//Given | Arrange

		//When | Act
		MvcResult result = mock.perform(MockMvcRequestBuilders.get("/musician/edit")).andReturn();
		//Then | Assert

		assertThat(result.getResponse().getStatus()).isEqualTo(200);

		ModelMap attributeMap = result.getModelAndView().getModelMap();
		Musician musician = (Musician) attributeMap.get("musician");
		assertThat(musician.getFirstName()).isEqualTo("Elke");
	}

	@Test
	void shouldBeAbleToUpdateElkesProfile() throws Exception {
		//Given | Arrange

		String newFirstName = "Welke";
		Address newAddress = new Address("53757", "Sankt Augustin");

		EditForm editForm = createEditFormWithChangedDataForElke(newFirstName, newAddress);

		//When
		MvcResult result = mock
				.perform(MockMvcRequestBuilders.post("/musician/edit")
						// csrf()) is required in post request, 
						// otherwise http response status will be 403 (forbidden)
						.with(SecurityMockMvcRequestPostProcessors.csrf()) 
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.flashAttr("musicianForm", editForm))
				.andReturn();
		//Then
		assertThat(result.getResponse().getStatus()).isEqualTo(302);

		List<Musician> listContainingWelke = musicianRepository
				.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("Welke", "e-gitarre");
		Musician welke = listContainingWelke.get(0);

		assertThat(welke.getAddress().getCity()).isEqualTo(newAddress.getCity());
	}

	private EditForm createEditFormWithChangedDataForElke(String newFirstName, Address newAddress) {
		List<Musician> listContainingElke = musicianRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("Elke", "e-gitarre");
		Musician elke = listContainingElke.get(0);

		EditForm editForm = new EditForm();
		editForm.setFirstName(newFirstName);
		editForm.setLastName(elke.getLastName());
		editForm.setDescription(elke.getDescription());
		editForm.setUsername(elke.getUsername());
		editForm.setUsernameRepeated(elke.getUsername());
		editForm.setPhone(elke.getPhone());
		editForm.setBirthday(elke.getBirthday());
		editForm.setGender(elke.getGender());
		editForm.setPostCode(newAddress.getPostCode());
		editForm.setCity(newAddress.getCity());
		editForm.setGenres(elke.getFavoriteGenres());
		editForm.setInstruments(elke.getInstruments());	
		editForm.setImage(elke.getImage());
		return editForm;
	}

}
