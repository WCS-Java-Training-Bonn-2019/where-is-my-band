package com.wildcodeschool.sea.bonn.whereismyband.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@SpringBootTest
@Transactional
class SearchControllerTest {

	MockMvc mock;

	@Autowired
	private BandRepository bandRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	private MusicianRepository musicianRepository;

	@BeforeEach
	void setup() {
		SearchController underTest = new SearchController(genreRepository, instrumentRepository, bandRepository, musicianRepository);
		mock = MockMvcBuilders.standaloneSetup(underTest).build();
	}

	@Test
	void shouldRetrieveSearchFormWithAllIntruments() throws Exception {

		//Given | Arrange

		//When | Act
		MvcResult result = mock.perform(MockMvcRequestBuilders.get("/search").with(user("elke@e-gitarre").password("elke"))).andReturn();
		//Then | Assert

		assertThat(result.getResponse().getStatus()).isEqualTo(200);

		ModelMap attributeMap = result.getModelAndView().getModelMap();
		List<Instrument> instruments = (List<Instrument>) attributeMap.get("allInstruments");
		assertThat(instruments).hasSize(instrumentRepository.findAll().size());

	}

	@Test
	void shouldBeAbleToLookForOpenPositions() throws Exception {
		//Given | Arrange
		int numberOfBandsToBeFound = bandRepository.findDistinctByBandPositionsState(PositionState.OFFEN).size();

		//When
		MvcResult result = mock.perform(MockMvcRequestBuilders.post("/search")
				.with(user("elke@e-gitarre").password("elke"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("positionState", "OFFEN")
				.param("zipcode", "")
				.param("city", "")
				.param("instrument", "")
				.param("genre", ""))
				.andReturn();
		//Then
		assertThat(result.getResponse().getStatus()).isEqualTo(200);

		ModelMap attributeMap = result.getModelAndView().getModelMap();
		List<Band> bands = (List<Band>) attributeMap.get("bands");

		assertThat(bands).hasSize(numberOfBandsToBeFound);
	}
	
	@Test
	void shouldFindDeTampentrekker() throws Exception {
		//Given | Arrange

		//When
		MvcResult result = mock.perform(MockMvcRequestBuilders.post("/search")
				.with(user("elke@e-gitarre").password("elke"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("positionState", "OFFEN")
				.param("zipcode", "21")
				.param("city", "hamburg")
				.param("instrument", "")
				.param("genre", "Schlager"))
				.andReturn();
		//Then
		assertThat(result.getResponse().getStatus()).isEqualTo(200);

		ModelMap attributeMap = result.getModelAndView().getModelMap();
		List<Band> bands = (List<Band>) attributeMap.get("bands");

		assertThat(bands.get(0).getName()).isEqualToIgnoringCase("De Tampentrekker");
	}

	@Test
	void shouldFindPaveier() throws Exception {
		//Given | Arrange

		//When
		MvcResult result = mock.perform(MockMvcRequestBuilders.post("/search")
				.with(user("elke@e-gitarre").password("elke"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("positionState", "BESETZT")
				.param("zipcode", "51429")
				.param("city", "")
				.param("instrument", "Schlagzeug")
				.param("genre", ""))
				.andReturn();
		//Then
		assertThat(result.getResponse().getStatus()).isEqualTo(200);

		ModelMap attributeMap = result.getModelAndView().getModelMap();
		List<Band> bands = (List<Band>) attributeMap.get("bands");

		assertThat(bands.get(0).getName()).isEqualToIgnoringCase("Paveier");
	}
	
	@Test
	void shouldFindNothing() throws Exception {
		//Given | Arrange

		//When
		MvcResult result = mock.perform(MockMvcRequestBuilders.post("/search")
				.with(user("elke@e-gitarre").password("elke"))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("positionState", "OFFEN")
				.param("zipcode", "11111")
				.param("city", "")
				.param("instrument", "")
				.param("genre", ""))
				.andReturn();
		//Then
		assertThat(result.getResponse().getStatus()).isEqualTo(200);

		ModelMap attributeMap = result.getModelAndView().getModelMap();
		List<Band> bands = (List<Band>) attributeMap.get("bands");

		assertThat(bands).isNull();
	}


}
