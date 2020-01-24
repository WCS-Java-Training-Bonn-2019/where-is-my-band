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

import com.wildcodeschool.sea.bonn.whereismyband.entity.Gender;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenderRepository;

@Controller
@RequestMapping("/gender")
public class GenderController {
	
	private GenderRepository genderRepository;
	
	@Autowired
	public GenderController(GenderRepository genderRepository) {
		super();
		this.genderRepository = genderRepository;
	}

	@GetMapping("list")
    public String getAll(Model model) {

        model.addAttribute("genders", genderRepository.findAll());

        return "genders";
    }

    @GetMapping("edit")
    public String getGender(Model model,
                            @RequestParam(required = false) Long id) {

        // Create an empty Gender object
    	Gender gender = new Gender();
        
    	// if an id was sent as a parameter
    	if (id != null) {
    		//retrieve object from database
            Optional<Gender> optionalGender = genderRepository.findById(id);
            // if database object could be retrieved
            if (optionalGender.isPresent()) {
            	// set gender to the object retrieved
                gender = optionalGender.get();
            }
        }
     	
        // add gender to the view model (either empty or prefilled with DB data)
    	model.addAttribute("gender", gender);

        return "gender";
    }

    @PostMapping("edit")
    public String postGender(@ModelAttribute Gender gender) {

        genderRepository.save(gender);

        return "redirect:list";
    }

    @GetMapping("delete")
    public String deleteGender(@RequestParam Long id) {

        genderRepository.deleteById(id);

        return "redirect:list";
    }
	
	

}
