package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.util.HashSet;
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
import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@Controller
@RequestMapping("/band")
public class BandController {

	private BandRepository bandRepository;
	private MusicianRepository musicianRepository;
	private GenreRepository genreRepository;
	private AddressRepository addressRepository;
	

	@Autowired
	public BandController(BandRepository bandRepository, MusicianRepository musicianRepository,
			GenreRepository genreRepository, AddressRepository addressRepository) {
		super();
		this.bandRepository = bandRepository;
		this.musicianRepository = musicianRepository;
		this.genreRepository = genreRepository;
		this.addressRepository = addressRepository;
	}

	@GetMapping("list")
	public String getAll(Model model) {
		model.addAttribute("bands", bandRepository.findAll());
		return "bands";
	}

	@GetMapping("edit")
	public String getBand(Model model,
			@RequestParam(required = false, name = "id") Long bandid,
			@RequestParam(required = false, name = "owner.id") Long ownerid) {

		// Create an empty Band object
		Band band = new Band();

		// if a bandid was sent as a parameter
		if (bandid != null) {
			//retrieve object from database
			Optional<Band> optionalBand = bandRepository.findById(bandid);
			// if database object could be retrieved
			if (optionalBand.isPresent()) {
				// set gender to the object retrieved
				band = optionalBand.get();
			}
		} else {
			// No band was sent => new band to be created
			// Retrieve owner from DB (important for new bands)
			Musician owner = musicianRepository.findById(ownerid).get();
			
			// if musician with ownerid given was found in DB
			if (owner != null) {
				// initialize band.owner
				band.setOwner(owner);
			}
		}

		// add band to the view model (either empty or prefilled with DB data)
		model.addAttribute("band", band);
		model.addAttribute("allGenres", new HashSet<Genre>(genreRepository.findAll()));

		return "bandupsert";
	}

	@PostMapping("edit")
	public String postBand(Model model, @ModelAttribute Band band) {
		addressRepository.save(band.getAddress());
		bandRepository.save(band);
		model.addAttribute(band);
		return "redirect:list";
	}

	@GetMapping("delete")
	public String deleteBand(@RequestParam Long id) {
		bandRepository.deleteById(id);
		return "redirect:list";
	}

	@GetMapping("view")
	public String viewBand(Model model,
			@RequestParam(required = false) Long id) {

		Band band = new Band();
		//retrieve object from database
		Optional<Band> optionalBand = bandRepository.findById(id);
		// if database object could be retrieved
		if (optionalBand.isPresent()) {
			// set gender to the object retrieved
			band = optionalBand.get();
		}

		// add band to the view model
		model.addAttribute("band", band);

		return "banddetails";
	}



}
