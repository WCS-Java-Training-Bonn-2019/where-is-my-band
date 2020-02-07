package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.io.IOException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.entity.RegistrationForm;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenderRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;
import com.wildcodeschool.sea.bonn.whereismyband.services.ImageService;

@Controller
public class RegistrationController {


	private final GenderRepository genderRepository;
	private final MusicianRepository musicianRepository;
	private final InstrumentRepository instrumentRepository;
	private final GenreRepository genreRepository;
	private final AddressRepository addressRepository;
	private final ImageService imageService;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public RegistrationController(GenderRepository genderRepository, MusicianRepository musicianRepository,
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

	@GetMapping("/register")
	public String getRegForm(Model model) {

		model.addAttribute("allGenders", genderRepository.findAll());
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("allGenres", genreRepository.findAll());

		// Create an empty RegistrationForm object
		RegistrationForm regForm= new RegistrationForm();

		// add (empty) RegForm object to the view model
		model.addAttribute("registrationForm", regForm);

		return "registration";
	}

	@PostMapping("/register")
	public String postRegForm( 
			@Valid RegistrationForm regForm, 
			BindingResult bindingResult, 
			@RequestParam("imagefile") MultipartFile file,
			Model model) {

		// Wenn Validierungsregeln nicht erfüllt
		if (bindingResult.hasErrors()) {
			// Zeige das Formular mit entsprechenden Fehlermeldungen wieder an
			model.addAttribute("allGenders", genderRepository.findAll());
			model.addAttribute("allInstruments", instrumentRepository.findAll());
			model.addAttribute("allGenres", genreRepository.findAll());
			return "registration";
		}

		// Prüfe, ob der Benutzername bereits existiert
		Optional<Musician> musicianOptionalFromDB = musicianRepository.findByUsername(regForm.getUsername());
		if (musicianOptionalFromDB.isPresent()) {
			model.addAttribute("message", "Der Benutzername \'"+ regForm.getUsername() + "\' existiert bereits in der Datenbank!");
			return "soundmachineerror";
		}
		
		// convert file into image byte array
		byte[] image=new byte[(int) file.getSize()];
		try {
			image = file.getBytes();
		
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String bildTyp = imageService.getImageType(image);
		
		if (! "JPEG".equals(bildTyp)) {
			model.addAttribute("message", "Bitte geben Sie eine Bilddatei im JPEG Format an!");
			return "soundmachineerror";			
		}

		Musician musician = new Musician();
		musician.setFirstName(regForm.getFirstName());
		musician.setLastName(regForm.getLastName());

		if (regForm.getDescription() != null) {
			musician.setDescription(regForm.getDescription());
		}

		musician.setUsername(regForm.getUsername());
		musician.setPassword(passwordEncoder.encode(regForm.getPassword()));
		musician.setPhone(regForm.getPhone());
		musician.setBirthday(regForm.getBirthday());
		musician.setGender(regForm.getGender());

		Address address = new Address();
		address.setCity(regForm.getCity());
		address.setPostCode(regForm.getPostCode());
		musician.setAddress(address);
		
		musician.setFavoriteGenres(regForm.getGenres());
		musician.setInstruments(regForm.getInstruments());

		addressRepository.save(musician.getAddress());
		musicianRepository.save(musician);
		imageService.saveImageFileMusician(musician.getId(), file);

		return "index";
	}
}
