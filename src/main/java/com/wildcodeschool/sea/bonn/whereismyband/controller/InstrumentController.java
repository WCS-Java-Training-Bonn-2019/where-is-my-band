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

import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;


@Controller
@RequestMapping("/instrument")
public class InstrumentController {
	
	private InstrumentRepository instrumentRepository;
	
	@Autowired
	public InstrumentController(InstrumentRepository instrumentRepository) {
		super();
		this.instrumentRepository = instrumentRepository;
	}

	@GetMapping("list")
    public String getAll(Model model) {

        model.addAttribute("instruments", instrumentRepository.findAll());

        return "instruments";
    }

    @GetMapping("edit")
    public String getInstrument(Model model,
                            @RequestParam(required = false) Long id) {

        model.addAttribute("allInstruments", instrumentRepository.findAll());
        // Create an empty Musician object
    	Instrument instrument = new Instrument();
        
    	// if an id was sent as a parameter
    	if (id != null) {
    		//retrieve object from database
            Optional<Instrument> optionalInstrument = instrumentRepository.findById(id);
            // if database object could be retrieved
            if (optionalInstrument.isPresent()) {
            	// set gender to the object retrieved
                instrument = optionalInstrument.get();
            }
        }
     	
        // add musician to the view model (either empty or prefilled with DB data)
    	model.addAttribute("instrument", instrument);

        return "instrument";
    }

    @PostMapping("edit")
    public String postInstrument(@ModelAttribute Instrument instrument) {

        instrumentRepository.save(instrument);

        return "redirect:list";
    }

    @GetMapping("delete")
    public String deleteInstrument(@RequestParam Long id) {

        instrumentRepository.deleteById(id);

        return "redirect:list";
    }
	
	

}
