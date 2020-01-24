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

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;

@Controller
@RequestMapping("/band")
public class BandController {
	
	private BandRepository bandRepository;
	
	@Autowired
	public BandController(BandRepository bandRepository) {
		super();
		this.bandRepository = bandRepository;
	}

	@GetMapping("list")
	public String getAll(Model model) {
		model.addAttribute("bands", bandRepository.findAll());
		return "bands";
	}
	
	 @GetMapping("edit")
	    public String getBand(Model model,
	                            @RequestParam(required = false) Long id) {

	        model.addAttribute("allBands", bandRepository.findAll());
	        // Create an empty Musician object
	    	Band band = new Band();
	        
	    	// if an id was sent as a parameter
	    	if (id != null) {
	    		//retrieve object from database
	            Optional<Band> optionalBand = bandRepository.findById(id);
	            // if database object could be retrieved
	            if (optionalBand.isPresent()) {
	            	// set gender to the object retrieved
	                band = optionalBand.get();
	            }
	        }
	     	
	        // add musician to the view model (either empty or prefilled with DB data)
	    	model.addAttribute("band", band);

	        return "band";
	    }
	
	@PostMapping("edit")
	public String postBand(@ModelAttribute Band band) {
		bandRepository.save(band);
		return "redirect:list";
	}

	@GetMapping("delete")
	public String deleteBand(@RequestParam Long id) {
		bandRepository.deleteById(id);
		return "redirect:list";
	}
	

}
