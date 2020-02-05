package com.wildcodeschool.sea.bonn.whereismyband.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;

@Controller
@RequestMapping("/search")
public class SearchController {

	private GenreRepository genreRepository;
	private InstrumentRepository instrumentRepository;
	private BandRepository bandRepository;
	
	@Autowired
	public SearchController(
		 	BandRepository bandRepository,
			GenreRepository genreRepository,
			InstrumentRepository instrumentRepository) {
		
		super();
		this.bandRepository = bandRepository;
		this.genreRepository = genreRepository;
		this.instrumentRepository = instrumentRepository;
	}
	
	@GetMapping("/")
	public String searchBands(Model model, 
			@RequestParam(required = false, name = "zipcode") String zipcode,
			@RequestParam(required = false, name = "city") String city,
			@RequestParam(required = false, name = "instrument") String instrument,
			@RequestParam(required = false, name = "genre") String genre) {
		List<Band> searchResult;

		if (zipcode != null) {
			if (!"".equals(city)) {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState.offen, zipcode, city, instrument, genre);
					} else {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndBandPositionsInstrumentName(PositionState.offen, zipcode, city, instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndFavoriteGenresName(PositionState.offen, zipcode, city, genre);
					} else {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCity(PositionState.offen, zipcode, city);
					}	
				}
			} else {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState.offen, zipcode, instrument, genre);
					} else {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentName(PositionState.offen, zipcode, instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndFavoriteGenresName(PositionState.offen, zipcode, genre);
					} else {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWith(PositionState.offen, zipcode);
					}	
				}

			}
		} else {
			if (!"".equals(city)) {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState.offen, city, instrument, genre);
					} else {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressCityAndBandPositionsInstrumentName(PositionState.offen, city, instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressCityAndFavoriteGenresName(PositionState.offen, city, genre);
					} else {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressCity(PositionState.offen, city);
					}	
				}
			} else {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndBandPositionsInstrumentNameAndFavoriteGenresName(PositionState.offen, instrument, genre);
					} else {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndBandPositionsInstrumentName(PositionState.offen, instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findDistinctByBandPositionsStateAndFavoriteGenresName(PositionState.offen, genre);
					} else {
						//kein Suchkriterium angegeben, also leere Liste ins Model
						searchResult = new ArrayList<>();
					}	
				}
			}
		}
		model.addAttribute("bands", searchResult);
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("allGenres", genreRepository.findAll());
		model.addAttribute("enteredZipcode", zipcode);
		model.addAttribute("enteredCity", city);
		model.addAttribute("selectedInstrument", instrument);
		model.addAttribute("selectedGenre", genre);

		return "bandsuche";
	}
	
	@GetMapping("list/open")
	public String getOpen(Model model) {
		
		model.addAttribute("bands", bandRepository.findDistinctByBandPositionsState(PositionState.offen));
		model.addAttribute("instruments", instrumentRepository.findAll());
		model.addAttribute("genres", genreRepository.findAll());
		return "bandsuche";
	}
	
	@GetMapping("list/all")
	public String getAll(Model model) {
		
		model.addAttribute("bands", bandRepository.findAll());
		model.addAttribute("instruments", instrumentRepository.findAll());
		model.addAttribute("genres", genreRepository.findAll());
		return "bandsuche";
	}
	
}
