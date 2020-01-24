package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;

@Controller
@RequestMapping("/bandposition")
public class BandpositionController{
	
	private BandpositionRepository bandpositionRepository;
	
	@Autowired
	public BandpositionController(BandpositionRepository bandpositionRepository) {
		super();
		this.bandpositionRepository = bandpositionRepository;
	}
	
	@GetMapping("list")
	public String getAll(Model model) {
		model.addAttribute("bandpositions", bandpositionRepository.findAll());
		
		return "bandpositions";
	}
	
	@GetMapping("edit")
	public String getBandposition(Model model, 
									@RequestParam(required = false) Long id) {
		
		model.addAttribute("allBandpositions", bandpositionRepository.findAll());
		
		Bandposition bandposition = new Bandposition();
		
		if (id != null) {
			Optional<Bandposition> optionalBandposition = bandpositionRepository.findById(id);
			
			if (optionalBandposition.isPresent()) {
				bandposition = optionalBandposition.get();
			}
		}
		
		model.addAttribute("bandposition", bandposition);
		
		return "bandposition";
	}
	
	@PostMapping("edit")
	public String postBandposition(@ModelAttribute Bandposition bandposition) {
		
		bandpositionRepository.save(bandposition);
		return "redirect:list";
	}
	
	@GetMapping("delete")
	public String deleteBandposition(@RequestParam Long id) {
		
		bandpositionRepository.deleteById(id);
		return "redirect:list";
	}
}
