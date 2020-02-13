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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.wildcodeschool.sea.bonn.whereismyband.services.ImageService;

@Controller
//@RequestMapping(value = { "/musician/{id}/", "/musician/", "/" })
@RequestMapping(value = { "/musician/", "/" })
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

	/**
	 * Returns the index.html - page
	 * @param model - includes the musician, if logged in
	 * @param principal
	 * @return the index.html
	 */
	@GetMapping("")
	public String getIndex(Model model, Principal principal) {

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());
			System.out.println("--- Der Username: " + principal.getName());
			model.addAttribute("musician", musicianOptional.get());
		}

		return "index";
	}

	@GetMapping("list")
	public String getAll(Model model) {

		model.addAttribute("musicians", musicianRepository.findAll());

		return "musicians";
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
		Musician musician = null;

		// bin ich eingeloggt?
		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());
			if (musicianOptional.isPresent()) {
				musician = musicianOptional.get();
			}
		}

		if (musician == null) {
			throw new RuntimeException("Musiker nicht gefunden!");
		}

		model.addAttribute("allGenders", genderRepository.findAll());
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("allGenres", genreRepository.findAll());

		model.addAttribute("musician", musician);
		model.addAttribute("isMusicianRegister", isMusicianRegister);

		EditForm editForm = new EditForm();
		
		//editForm mit Musikerdaten vorbelegen
		editForm.setFirstName(musician.getFirstName());
		editForm.setLastName(musician.getLastName());
		editForm.setDescription(musician.getDescription());
		editForm.setImage(musician.getImage());
		editForm.setUsername(musician.getUsername());
		editForm.setUsernameRepeated(musician.getUsername());
		editForm.setPhone(musician.getPhone());
		editForm.setBirthday(musician.getBirthday());
		editForm.setGender(musician.getGender());
		editForm.setPostCode(musician.getAddress().getPostCode());
		editForm.setCity(musician.getAddress().getCity());
		editForm.setGenres(musician.getFavoriteGenres());
		editForm.setInstruments(musician.getInstruments());
		model.addAttribute("registrationForm", editForm);

		return "musicianupsert";
	}

	/**
	 * Controller which processes the editing form for a musician
	 * ToDo: Route checken, darf der Benutzername geändert werden, Passwort (alt) eingeben
	 * @param editForm
	 * @param bindingResult
	 * @param principal
	 * @param model
	 * @return the musician detail page, if all went fine, otherwise the editing form again
	 */
	@PostMapping("/edit")
	public String editMusicianPost(

			@Valid EditForm editForm, BindingResult bindingResult, Principal principal, Model model) {

		boolean isMusicianRegister = false;
		Musician musician = null;

		// bin ich eingeloggt?
		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());
			if (musicianOptional.isPresent()) {
				musician = musicianOptional.get();
			}
		}

		if (musician == null) {
			throw new RuntimeException("Musiker nicht gefunden!");
		}

		// Wenn Validierungsregeln nicht erfüllt
		if (bindingResult.hasErrors()) {
			// Zeige das Formular mit entsprechenden Fehlermeldungen wieder an
			model.addAttribute("allGenders", genderRepository.findAll());
			model.addAttribute("allInstruments", instrumentRepository.findAll());
			model.addAttribute("allGenres", genreRepository.findAll());
			model.addAttribute("isMusicianRegister", isMusicianRegister);
			model.addAttribute("musician", musician);
			return "musicianupsert";
		}

//		darf Benutzername geändert werden?
//		if (principal == null) {
//			// Prüfe, ob der Benutzername bereits existiert
//			Optional<Musician> musicianOptionalFromDB = musicianRepository.findByUsername(editForm.getUsername());
//			if (musicianOptionalFromDB.isPresent()) {
//				model.addAttribute("message", "Der Benutzername \'"+ editForm.getUsername() + "\' existiert bereits in der Datenbank!");
//				return "soundmachineerror";
//			}
//		} 

		musician.setFirstName(editForm.getFirstName());
		musician.setLastName(editForm.getLastName());

		if (editForm.getDescription() != null) {
			musician.setDescription(editForm.getDescription());
		}

		musician.setUsername(editForm.getUsername());
		if (!editForm.getPassword().equals("")) {
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
		musician.setImage(editForm.getImage());

		addressRepository.save(musician.getAddress());
		musicianRepository.save(musician);

		return "redirect:view";
	}


	/**
	 * @param model
	 * @param principal
	 * @return
	 */
	@GetMapping("view")
	public String viewMusician(Model model, Principal principal) {

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

			if (musicianOptional.isPresent()) {
				model.addAttribute("musician", musicianOptional.get());
			} else {
				throw new RuntimeException("Musiker nicht gefunden!");
			}
		}

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

			// get image from Optional
			Musician musician = musicianOptional.get();

			// set result type to http response
			response.setContentType("image/jpeg");

			// write ByteArray to http response
			InputStream is = new ByteArrayInputStream(musician.getImage());
			IOUtils.copy(is, response.getOutputStream());
		}
	}
	
	@GetMapping("delete")
	public String deleteMusician(@PathVariable Long id) {

		musicianRepository.deleteById(id);

		return "redirect:list";
	}
}
