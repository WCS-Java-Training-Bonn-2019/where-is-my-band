package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenderRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;
import com.wildcodeschool.sea.bonn.whereismyband.services.ImageService;

@Controller
@RequestMapping(value = {"/musician/{id}/", "/musician/", "/"})
public class MusicianController {


	private final GenderRepository genderRepository;
	private final MusicianRepository musicianRepository;
	private final InstrumentRepository instrumentRepository;
	private final GenreRepository genreRepository;
	private final AddressRepository addressRepository;
	private final ImageService imageService;
	private final PasswordEncoder passwordEncoder;
	private final BandRepository bandRepository;


	
	@Autowired
	public MusicianController(GenderRepository genderRepository, MusicianRepository musicianRepository,
			InstrumentRepository instrumentRepository, GenreRepository genreRepository,
			AddressRepository addressRepository, ImageService imageService, PasswordEncoder passwordEncoder,
			BandRepository bandRepository) {
		super();
		this.genderRepository = genderRepository;
		this.musicianRepository = musicianRepository;
		this.instrumentRepository = instrumentRepository;
		this.genreRepository = genreRepository;
		this.addressRepository = addressRepository;
		this.imageService = imageService;
		this.passwordEncoder = passwordEncoder;
		this.bandRepository = bandRepository;
	}

	@GetMapping("")
	public String getIndex(Model model, Principal principal) {

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

			model.addAttribute("musician", musicianOptional.get());
			
		}

		return "index";
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
	public String postMusician(@PathVariable (name = "id", required=false) Long id, @ModelAttribute Musician musician) {

		if("admin".equals(musician.getUsername())) {
			throw new IllegalArgumentException("admin not allowed here");
		}
		musician.setPassword(passwordEncoder.encode(musician.getPassword()));
		musician = musicianRepository.save(musician);

		return "redirect:list";
	}

	@GetMapping("delete")
	public String deleteMusician(@PathVariable Long id) {

		musicianRepository.deleteById(id);

		return "redirect:list";
	}

	@GetMapping("view")
	public String viewMusician(@PathVariable Long id, Model model) {

		Optional<Musician> musicianOptional = musicianRepository.findById(id);

		if (! musicianOptional.isPresent()) {
			throw new IllegalArgumentException("Musiker ID icht gefunden!");
		}
		model.addAttribute("musician", musicianOptional.get());

		return "musiciandetails";
	}


	// Via this route, the image can be retrieved for display via an HTML image element <img ...>
	@GetMapping("image")
	public void renderImageFromDB(@PathVariable Long id, HttpServletResponse response) throws IOException {

		// retrieve band from DB
		Optional<Musician> musicianOptional = musicianRepository.findById(id);

		// if band was found and image exists
		if (musicianOptional.isPresent() && musicianOptional.get().getImage() != null) {

			// get image from Optional
			Musician musician = musicianOptional.get();

			// set result type to http response
			response.setContentType("image/jpeg");

			// write ByteArray to http response
			InputStream is = new ByteArrayInputStream(musician.getImage());
			IOUtils.copy(is, response.getOutputStream());
		}
	}
	
	private Musician assertValidUser(Principal principal) {
		Optional<Musician> userOptional = musicianRepository.findByUsername(principal.getName());

		// musician matching the principal was found in DB
		if (!userOptional.isPresent()) {
			throw new IllegalArgumentException("Angemeldeter Benutzer wurde nicht in der Datenbank gefunden");
		}

		Musician musicianLoggedIn = userOptional.get();
		return musicianLoggedIn;
	}
}
