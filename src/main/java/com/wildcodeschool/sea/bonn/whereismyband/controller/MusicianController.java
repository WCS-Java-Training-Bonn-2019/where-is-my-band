package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
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


	@Autowired
	public MusicianController(GenderRepository genderRepository, MusicianRepository musicianRepository,
			InstrumentRepository instrumentRepository, GenreRepository genreRepository,
			AddressRepository addressRepository, ImageService imageService, PasswordEncoder passwordEncoder) {
		super();
		this.genderRepository = genderRepository;
		this.musicianRepository = musicianRepository;
		this.instrumentRepository = instrumentRepository;
		this.genreRepository = genreRepository;
		this.addressRepository = addressRepository;
		this.imageService = imageService;
		this.passwordEncoder = passwordEncoder;
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

	@GetMapping("register")
	public String getRegForm(Model model) {

		model.addAttribute("allGenders", genderRepository.findAll());
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("allGenres", genreRepository.findAll());

		// Create an empty Musician object
		Musician musician = new Musician();

		// add (empty) musician to the view model
		model.addAttribute("musician", musician);

		return "registration";
	}

	@PostMapping("register")
	public String postRegForm(Model model, 
			@Valid Musician musician, 
			BindingResult bindingResult, 
			@RequestParam (name = "passwordRepeated", required = true) String pwRepeated,
			@RequestParam("imagefile") MultipartFile file) {

		// Wenn Validierungsregeln nicht erfüllt
		if (bindingResult.hasErrors()) {
			// Zeige das Formular mit entsprechenden Fehlermeldungen wieder an
			model.addAttribute("allGenders", genderRepository.findAll());
			model.addAttribute("allInstruments", instrumentRepository.findAll());
			model.addAttribute("allGenres", genreRepository.findAll());
			return "registration";
		}

		// Prüfe, ob gesetztes Passwort gesetzt ist und dem pwRepeated entspricht
		if ((pwRepeated == null) 
				|| (musician.getPassword() == null) 
				|| ! pwRepeated.equals(musician.getPassword())) {
			throw new IllegalArgumentException("Passwort nicht gesetzt bzw. nicht identisch");
		}

		addressRepository.save(musician.getAddress());
		musician.setPassword(passwordEncoder.encode(musician.getPassword()));
		musicianRepository.save(musician);
		imageService.saveImageFileMusician(musician.getId(), file);

		return "index";
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
}
