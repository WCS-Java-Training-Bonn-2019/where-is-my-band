package com.wildcodeschool.sea.bonn.whereismyband.controller;


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
			@RequestParam(required = false, name = "zipcode") Integer zipcode,
			@RequestParam(required = false, name = "city") String city,
			@RequestParam(required = false, name = "instrument") String instrument,
			@RequestParam(required = false, name = "genre") String genre) {
		List<Band> searchResult;
		System.out.println(zipcode + "+" + city + "+" + instrument + "+" + genre);
		if (zipcode != null) {
			if (!"".equals(city)) {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByAddressPostCodeAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(zipcode, city, instrument, genre);
					} else {
						searchResult = bandRepository.findByAddressPostCodeAndAddressCityAndBandPositionsInstrumentName(zipcode, city, instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByAddressPostCodeAndAddressCityAndFavoriteGenresName(zipcode, city, genre);
					} else {
						searchResult = bandRepository.findByAddressPostCodeAndAddressCity(zipcode, city);
					}	
				}
			} else {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByAddressPostCodeAndBandPositionsInstrumentNameAndFavoriteGenresName(zipcode, instrument, genre);
					} else {
						searchResult = bandRepository.findByAddressPostCodeAndBandPositionsInstrumentName(zipcode, instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByAddressPostCodeAndFavoriteGenresName(zipcode, genre);
					} else {
						searchResult = bandRepository.findByAddressPostCode(zipcode);
					}	
				}

			}
		} else {
			if (!"".equals(city)) {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(city, instrument, genre);
					} else {
						searchResult = bandRepository.findByAddressCityAndBandPositionsInstrumentName(city, instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByAddressCityAndFavoriteGenresName(city, genre);
					} else {
						searchResult = bandRepository.findByAddressCity(city);
					}	
				}
			} else {
				if (!"".equals(instrument)) {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByBandPositionsInstrumentNameAndFavoriteGenresName(instrument, genre);
					} else {
						searchResult = bandRepository.findByBandPositionsInstrumentName(instrument);
					}					
				} else {
					if (!"".equals(genre)) {
						searchResult = bandRepository.findByFavoriteGenresName(genre);
					} else {
						//kein Suchkriterium angegeben, hier nur um den Compiler zufriedenzustellen
						searchResult = bandRepository.findByAddressCity("");
					}	
				}

			}

		}
		model.addAttribute("bands", searchResult);

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
