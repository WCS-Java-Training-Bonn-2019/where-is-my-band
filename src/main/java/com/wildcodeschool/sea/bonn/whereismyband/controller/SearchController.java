package com.wildcodeschool.sea.bonn.whereismyband.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;

@Controller
@RequestMapping("/search")
public class SearchController {

	private BandpositionRepository bandpositionRepository;
	private GenreRepository genreRepository;
	private InstrumentRepository instrumentRepository;
	private AddressRepository addressRepository;
	private BandRepository bandRepository;
	
	@Autowired
	public SearchController(
			BandpositionRepository bandpositionRepository,
			AddressRepository addressRepository,
		 	BandRepository bandRepository,
			GenreRepository genreRepository,
			InstrumentRepository instrumentRepository) {
		
		super();
		this.bandpositionRepository = bandpositionRepository;
		this.addressRepository = addressRepository;
		this.bandRepository = bandRepository;
		this.genreRepository = genreRepository;
		this.instrumentRepository = instrumentRepository;
	}
	
	@GetMapping("list")
	public String getAll(Model model) {
		
		model.addAttribute("bands", bandRepository.findAll());
		model.addAttribute("bandpositions", bandpositionRepository.findAll());
		model.addAttribute("addresses", addressRepository.findAll());
		model.addAttribute("instruments", instrumentRepository.findAll());
		model.addAttribute("genres", genreRepository.findAll());
		return "bandsuche_dynamisch";
	}
	
}
