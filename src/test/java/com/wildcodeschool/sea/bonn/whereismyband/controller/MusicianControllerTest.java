package com.wildcodeschool.sea.bonn.whereismyband.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import com.wildcodeschool.sea.bonn.whereismyband.entity.EditForm;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenderRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
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
	private GenderRepository genderRepository;

	@Autowired
	private MusicianRepository musicianRepository;

	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	void setup() {
		MusicianController underTest = new MusicianController(
				genderRepository, 
				musicianRepository, 
				instrumentRepository, 
				genreRepository, 
				addressRepository, 
				passwordEncoder);

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
		@SuppressWarnings("unchecked")
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
		@SuppressWarnings("unchecked")
		Musician musician = (Musician) attributeMap.get("musician");
		assertThat(musician.getFirstName()).isEqualTo("Elke");

	}

	@Test
	void shouldBeAbleToUpdateElkesProfile() throws Exception {
		//Given | Arrange
		List<Musician> listContainingElke = musicianRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase("Elke", "e-gitarre");
		Musician elke = listContainingElke.get(0);
		String newCity = elke.getAddress().getCity() + "-Zentrum";

		EditForm editForm = new EditForm();
		editForm.setFirstName("Welke");
		editForm.setLastName(elke.getLastName());
		editForm.setDescription(elke.getDescription());
		editForm.setUsername(elke.getUsername());
		editForm.setUsernameRepeated(elke.getUsername());
		editForm.setPhone(elke.getPhone());
		editForm.setBirthday(elke.getBirthday());
		editForm.setGender(elke.getGender());
		editForm.setPostCode(elke.getAddress().getPostCode());
		editForm.setCity(newCity);
		editForm.setGenres(elke.getFavoriteGenres());
		editForm.setInstruments(elke.getInstruments());	
		editForm.setImage(elke.getImage());

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

		assertThat(welke.getAddress().getCity()).isEqualTo(newCity);

	}

}
