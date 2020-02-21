package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@Controller
public class SearchController {

	private GenreRepository genreRepository;
	private InstrumentRepository instrumentRepository;
	private BandRepository bandRepository;
	private final MusicianRepository musicianRepository;

	@Autowired
	public SearchController(GenreRepository genreRepository, InstrumentRepository instrumentRepository,
			BandRepository bandRepository, MusicianRepository musicianRepository) {
		super();
		this.genreRepository = genreRepository;
		this.instrumentRepository = instrumentRepository;
		this.bandRepository = bandRepository;
		this.musicianRepository = musicianRepository;
	}

	@GetMapping("/search")
	public String searchBandsGet(Model model, Principal principal) {

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

			model.addAttribute("musician", musicianOptional.get());
		}

		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("allGenres", genreRepository.findAll());
		model.addAttribute("allPositionStates", PositionState.values());
		
		return "Band/bandsearch";

		}

	
	@PostMapping("/search")
	public String searchBandsPost(Model model, Principal principal,
			@RequestParam(required = true, name ="positionState") PositionState positionState,
			@RequestParam(required = false, name = "zipcode") String postCode,
			@RequestParam(required = false, name = "city") String city,
			@RequestParam(required = false, name = "instrument") String instrument,
			@RequestParam(required = false, name = "genre") String genre) {

		List<Band> searchResult = null;

		// if only postCode or citySet are set
		if 	( ((postCode != null && postCode.length() > 0) || (city != null &&  city.length() > 0))
				&& "".equals(instrument)
				&& "".equals(genre)) {
			searchResult = searchBandsByPostcodeOrCityViaExample(postCode, city);
		} else {

			if (!"".equals(postCode)) {
				if (!"".equals(city)) {
					if (!"".equals(instrument)) {

						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(positionState, postCode, city, instrument, genre);
						} else {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndBandPositionsInstrumentName(positionState, postCode, city, instrument);
						}					
					} else {
						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndAddressCityAndFavoriteGenresName(positionState, postCode, city, genre);
						} else {
							// postCode and city are set / instrument and genre are not set => covered by searchBandsByPostcodeOrCityViaExample()
						}	
					}
				} else {
					// postcode is set, city is not set
					if (!"".equals(instrument)) {
						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentNameAndFavoriteGenresName(positionState, postCode, instrument, genre);
						} else {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndBandPositionsInstrumentName(positionState, postCode, instrument);
						}					
					} else {
						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressPostCodeStartingWithAndFavoriteGenresName(positionState, postCode, genre);
						} else {
							// postCode is set / city, instrument and genre are not set => covered by searchBandsByPostcodeOrCityViaExample()						}	
						}
					}
				}
			} else {
				// postCode is not set
				if (!"".equals(city)) {
					if (!"".equals(instrument)) {
						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressCityAndBandPositionsInstrumentNameAndFavoriteGenresName(positionState, city, instrument, genre);
						} else {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressCityAndBandPositionsInstrumentName(positionState, city, instrument);
						}					
					} else {
						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndAddressCityAndFavoriteGenresName(positionState, city, genre);
						} else {
							// city is set / postCode, instrument and genre are not set => covered by searchBandsByPostcodeOrCityViaExample()						}	
						}	
					}
				} else {
					if (!"".equals(instrument)) {
						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndBandPositionsInstrumentNameAndFavoriteGenresName(positionState, instrument, genre);
						} else {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndBandPositionsInstrumentName(positionState, instrument);
						}					
					} else {
						if (!"".equals(genre)) {
							searchResult = bandRepository.findDistinctByBandPositionsStateAndFavoriteGenresName(positionState, genre);
						} else {
							//kein Suchkriterium angegeben, also leere Liste ins Model
							searchResult = bandRepository.findAll();
						}	
					}
				}
			}
		}

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

			model.addAttribute("musician", musicianOptional.get());
		}

		model.addAttribute("bands", searchResult);
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("allGenres", genreRepository.findAll());
		model.addAttribute("allPositionStates", PositionState.values());
		model.addAttribute("selectedState", positionState);
		model.addAttribute("enteredZipcode", postCode);
		model.addAttribute("enteredCity", city);
		model.addAttribute("selectedInstrument", instrument);
		model.addAttribute("selectedGenre", genre);

		return "Band/bandsearch";
	}

	@GetMapping("/search/list/open")
	public String getOpen(Model model) {

		model.addAttribute("bands", bandRepository.findDistinctByBandPositionsState(PositionState.OFFEN));
		model.addAttribute("instruments", instrumentRepository.findAll());
		model.addAttribute("genres", genreRepository.findAll());
		return "Band/bandsearch";
	}

	@GetMapping("/search/list/all")
	public String getAll(Model model) {

		model.addAttribute("bands", bandRepository.findAll());
		model.addAttribute("instruments", instrumentRepository.findAll());
		model.addAttribute("genres", genreRepository.findAll());
		return "Band/bandsearch";
	}

	private List<Band> searchBandsByPostcodeOrCityViaExample(String postCode, String city) {

		Band exampleBand = new Band();

		// initialize address in exampleBand, if one of them was set
		boolean postCodeIsSet = (postCode != null && postCode.length() > 0);
		boolean cityIsSet = (city != null && city.length() > 0);

		List<Band> searchResult;
		Address exampleAddress = new Address();		
		// set search address in example band
		if (postCodeIsSet)
			exampleAddress.setPostCode(postCode);
		if (cityIsSet) 
			exampleAddress.setCity(city);
		exampleBand.setAddress(exampleAddress);		

		ExampleMatcher exampleMatcher = ExampleMatcher
				.matchingAll()
				.withMatcher("address.postCode", ExampleMatcher.GenericPropertyMatchers.startsWith())
				.withMatcher("address.city", ExampleMatcher.GenericPropertyMatchers.startsWith().ignoreCase());

		searchResult = 
				bandRepository.findAll(Example.of(exampleBand, exampleMatcher));

		return searchResult;
	}

}
