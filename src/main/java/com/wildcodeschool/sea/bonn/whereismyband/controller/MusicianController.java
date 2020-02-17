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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.EditForm;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenderRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;

@Controller
@RequestMapping(value = { "/musician/", "/" })
public class MusicianController {

	private final GenderRepository genderRepository;
	private final MusicianRepository musicianRepository;
	private final InstrumentRepository instrumentRepository;
	private final GenreRepository genreRepository;
	private final AddressRepository addressRepository;
	private final PasswordEncoder passwordEncoder;


	@Autowired
	public MusicianController(GenderRepository genderRepository, MusicianRepository musicianRepository,
			InstrumentRepository instrumentRepository, GenreRepository genreRepository,
			AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
		super();
		this.genderRepository = genderRepository;
		this.musicianRepository = musicianRepository;
		this.instrumentRepository = instrumentRepository;
		this.genreRepository = genreRepository;
		this.addressRepository = addressRepository;
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * Returns the index.html - page
	 * @param model - includes the musician, if logged in
	 * @param principal
	 * @return the index.html
	 */
	@GetMapping("")
	public String getIndex(Model model, Principal principal) {
		if (principal != null) {
			Musician musician = getMusicianLoggedInFromDB(principal);
			model.addAttribute("musician", musician);
		}

		return "index";
	}


	/**
	 * Controller which returns the editing form for a musician
	 * @param model includes the musician and all of his attributes to prefill the form
	 * @param principal
	 * @return the page for editing a musician
	 */
	@GetMapping("/edit")
	public String editMusicianGet(Model model, Principal principal) {

		boolean isMusicianRegister = false;
		Musician musician = getMusicianLoggedInFromDB(principal);

		model.addAttribute("allGenders", genderRepository.findAll());
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("allGenres", genreRepository.findAll());

		model.addAttribute("musician", musician);
		model.addAttribute("isMusicianRegister", isMusicianRegister);

		EditForm editForm = new EditForm();
		
		//prefill editForm with musician's data
		editForm.setFirstName(musician.getFirstName());
		editForm.setLastName(musician.getLastName());
		editForm.setDescription(musician.getDescription());
		editForm.setUsername(musician.getUsername());
		editForm.setUsernameRepeated(musician.getUsername());
		editForm.setPhone(musician.getPhone());
		editForm.setBirthday(musician.getBirthday());
		editForm.setGender(musician.getGender());
		editForm.setPostCode(musician.getAddress().getPostCode());
		editForm.setCity(musician.getAddress().getCity());
		editForm.setGenres(musician.getFavoriteGenres());
		editForm.setInstruments(musician.getInstruments());
		model.addAttribute("musicianForm", editForm);

		return "musicianupsert";
	}

	/**
	 * Controller which processes the editing form for a musician
	 * @param editForm
	 * @param bindingResult
	 * @param principal
	 * @param model
	 * @return the musician detail page, if all went fine, otherwise the editing form again
	 */
	@PostMapping("/edit")
	// Dem "form backing bean" muss hier explizit mit "@ModelAttribute" ein Namen gegeben werden, da der th:object-Tag im Template nicht auf den Klassennamen referenziert (hier "EditForm")
	// http://forum.thymeleaf.org/Fields-object-functions-Spring-td3302513.html
	public String editMusicianPost(
			@Valid @ModelAttribute("musicianForm") EditForm editForm, 
			BindingResult bindingResult, 
			Principal principal, 
			Model model) {

		boolean isMusicianRegister = false;
		Musician musician = getMusicianLoggedInFromDB(principal);

		// Wenn Validierungsregeln nicht erfüllt, zeige das Formular mit entsprechenden Fehlermeldungen wieder an.
		// Die Form wird automatisch über die form backing bean gefüllt
		// https://www.baeldung.com/spring-mvc-form-tutorial
		if (bindingResult.hasErrors()) {
			model.addAttribute("allGenders", genderRepository.findAll());
			model.addAttribute("allInstruments", instrumentRepository.findAll());
			model.addAttribute("allGenres", genreRepository.findAll());
			model.addAttribute("isMusicianRegister", isMusicianRegister);
			model.addAttribute("musician", musician);
			return "musicianupsert";
		}

		updateMusicianFromEditForm(editForm, musician);
		
		addressRepository.save(musician.getAddress());
		musicianRepository.save(musician);

		return "redirect:view";
	}

	/**
	 * @param model
	 * @param principal
	 * @return the Userdetail-Page
	 */
	@GetMapping("view")
	public String viewMusician(Model model, Principal principal) {
		model.addAttribute("musician", getMusicianLoggedInFromDB(principal));

		return "musiciandetails";
	}

	
	/**
	 * Via this route, the image can be retrieved for display via an HTML image element <img ...>
	 * @param principal
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("image")
	public void renderImageFromDB(Principal principal, HttpServletResponse response) throws IOException {

		// retrieve musician
		Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

		// if musician was found and image exists
		if (musicianOptional.isPresent() && musicianOptional.get().getImage() != null) {

			// get musician from Optional
			Musician musician = musicianOptional.get();

			// set result type to http response
			response.setContentType("image/jpeg");

			// write ByteArray to http response
			InputStream is = new ByteArrayInputStream(musician.getImage());
			IOUtils.copy(is, response.getOutputStream());
		}
	}
	
	
	/**
	 * Returns the musician object found by the username of the loggedin user
	 * @param principal
	 * @return The musician found in DB by userName
	 */
	private Musician getMusicianLoggedInFromDB(Principal principal) {
		Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

		if (!musicianOptional.isPresent()) {
			throw new IllegalArgumentException("Angemeldeter Benutzer wurde nicht in der Datenbank gefunden.");
		}

		Musician musicianLoggedIn = musicianOptional.get();
		return musicianLoggedIn;
	}
	

	/**
	 * @param editForm The form backing bean with updated musician's data
	 * @param musician The musician with changed musician data
	 */
	private void updateMusicianFromEditForm(EditForm editForm, Musician musician) {
		musician.setFirstName(editForm.getFirstName());
		musician.setLastName(editForm.getLastName());
	
		if (editForm.getDescription() != null) {
			musician.setDescription(editForm.getDescription());
		}
	
	
		if (!editForm.getPassword().isEmpty()) {
			musician.setPassword(passwordEncoder.encode(editForm.getPassword()));
		}
		
		musician.setPhone(editForm.getPhone());
		musician.setBirthday(editForm.getBirthday());
		musician.setGender(editForm.getGender());
	
		Address address = new Address();
		address.setCity(editForm.getCity());
		address.setPostCode(editForm.getPostCode());
		musician.setAddress(address);
	
		musician.setFavoriteGenres(editForm.getGenres());
		musician.setInstruments(editForm.getInstruments());
		
		if (editForm.getImage().length != 0) {
			musician.setImage(editForm.getImage());
		}
	}
}
