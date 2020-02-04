package com.wildcodeschool.sea.bonn.whereismyband.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
