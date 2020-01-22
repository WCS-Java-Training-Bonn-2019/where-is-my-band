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

import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenderRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@Controller
@RequestMapping("/musician")
public class MusicianController {
	
	private GenderRepository genderRepository;
	private MusicianRepository musicianRepository;
	
	@Autowired
	public MusicianController(GenderRepository genderRepository, MusicianRepository musicianRepository) {
		super();
		this.genderRepository = genderRepository;
		this.musicianRepository = musicianRepository;
	}

	@GetMapping("list")
    public String getAll(Model model) {

        model.addAttribute("musicians", musicianRepository.findAll());

        return "musicians";
    }

    @GetMapping("edit")
    public String getMusician(Model model,
                            @RequestParam(required = false) Long id) {

        model.addAttribute("allGenders", genderRepository.findAll());
        // Create an empty Musician object
    	Musician musician = new Musician();
        
    	// if an id was sent as a parameter
    	if (id != null) {
    		//retrieve object from database
            Optional<Musician> optionalMusician = musicianRepository.findById(id);
            // if database object could be retrieved
            if (optionalMusician.isPresent()) {
            	// set gender to the object retrieved
                musician = optionalMusician.get();
            }
        }
     	
        // add musician to the view model (either empty or prefilled with DB data)
    	model.addAttribute("musician", musician);

        return "musician";
    }

    @PostMapping("edit")
    public String postMusician(@ModelAttribute Musician musician) {

        musicianRepository.save(musician);

        return "redirect:list";
    }

    @GetMapping("delete")
    public String deleteMusician(@RequestParam Long id) {

        musicianRepository.deleteById(id);

        return "redirect:list";
    }
	
	

}
