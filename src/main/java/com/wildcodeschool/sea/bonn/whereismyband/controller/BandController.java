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

import com.wildcodeschool.sea.bonn.whereismyband.entity.Address;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Bandposition;
import com.wildcodeschool.sea.bonn.whereismyband.entity.EditForm;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Instrument;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.InstrumentRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;
import com.wildcodeschool.sea.bonn.whereismyband.services.ImageService;

@Controller
@RequestMapping( {"/band/"})
public class BandController {

	private final BandRepository bandRepository;
	private final MusicianRepository musicianRepository;
	private final GenreRepository genreRepository;
	private final AddressRepository addressRepository;
	private final BandpositionRepository bandPositionsRepository;
	private final ImageService imageService;
	private final InstrumentRepository instrumentRepository;

	@Autowired
	public BandController(BandRepository bandRepository, MusicianRepository musicianRepository,
			GenreRepository genreRepository, AddressRepository addressRepository,
			BandpositionRepository bandPositionsRepository, ImageService imageService,
			InstrumentRepository instrumentRepository) {
		super();
		this.bandRepository = bandRepository;
		this.musicianRepository = musicianRepository;
		this.genreRepository = genreRepository;
		this.addressRepository = addressRepository;
		this.bandPositionsRepository = bandPositionsRepository;
		this.imageService = imageService;
		this.instrumentRepository = instrumentRepository;
	}

	@GetMapping("{id}/edit")
	public String editBandGet(
			Model model, 
			Principal principal,
			@PathVariable(name = "id") Long bandid) {

		Band band = null;
		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = assertValidUser(principal);	
			band = createOrRetrieveBand(bandid, musicianLoggedIn);
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// add band to the view model
		initializeBandFormModel(model, band, musicianLoggedIn);
		return "Band/bandupsert";
	}

	@PostMapping("{id}/edit")
	public String editBandPost(
			Model model,
			Principal principal,
			@ModelAttribute Band band, 
			@PathVariable Long id) {

		Musician musicianLoggedIn = null;
		Band bandFromDB =null;

		try {
			musicianLoggedIn = assertValidUser(principal);
			bandFromDB = createOrRetrieveBand(id, musicianLoggedIn);
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// DB contains an image for this band
		if (bandFromDB.getImage() != null) {
			// set image of band to the one stored n the DB
			band.setImage(bandFromDB.getImage());
		}

		// save band.address in DB
		addressRepository.save(band.getAddress());
		// save band.bandpositions in DB
		bandPositionsRepository.saveAll(band.getBandPositions());
		// save band attributes in DB
		bandRepository.save(band);

		
		model.addAttribute(band);
		model.addAttribute("musician", musicianLoggedIn);
		
		

		return "redirect:/band/" + band.getId() + "/view";
	}

	@GetMapping("new")
	public String newBandGet(
			Model model, 
			Principal principal) {

		Band band = null;
		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = assertValidUser(principal);	
			band = createOrRetrieveBand(null, musicianLoggedIn);
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// add band to the view model (either empty)
		initializeBandFormModel(model, band, musicianLoggedIn);
		return "Band/bandupsert";
	}


	@PostMapping("new")
	public String newBandPost(
			Model model,
			Principal principal,
			@ModelAttribute Band band) {

		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = assertValidUser(principal);	
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// set owner to the musician currently logged in
		band.setOwner(musicianLoggedIn);

		// save band.address in DB
		addressRepository.save(band.getAddress());

		// save band.bandpositions in DB
		bandPositionsRepository.saveAll(band.getBandPositions());

		// save band attributes in DB
		bandRepository.save(band);

		model.addAttribute(band);
		model.addAttribute("musician", musicianLoggedIn);

		return "redirect:/band/" + band.getId() + "/view";
	}

	// create a new Position for the band identified by ID
	@PostMapping("{id}/newbandposition")
	public String newBandposition(
			Model model,
			Principal principal,
			@PathVariable(required = true, name = "id") Long bandID,
			@RequestParam(required = true, name = "instrID") Long instrID) {

		Band band = null;
		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = assertValidUser(principal);	
			band = createOrRetrieveBand(bandID, musicianLoggedIn);
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// look for instrument in DB
		Optional<Instrument> instrumentOptional = instrumentRepository.findById(instrID);
		// if instrument could not be found
		if (!instrumentOptional.isPresent()) {
			model.addAttribute("message", " Instrument with this ID could not be found");
			return "soundmachineerror";
		}
		Instrument instrument = instrumentOptional.get();

		// create bandposition with the instrument identified by instrID for band
		Bandposition bandPosition = new Bandposition();
		bandPosition.setInstrument(instrument);
		bandPosition.setBand(band);
		bandPosition.setState(PositionState.OFFEN);
		bandPosition.setAgeFrom(20);
		bandPosition.setAgeTo(70);
		bandPositionsRepository.save(bandPosition);

		return "redirect:/band/" + bandID + "/edit";
	}

	// KrillMi, 10.02.2020: Deletes the position identified by posID for the band
	// identified by id
	@GetMapping("{id}/deletebandposition/{posID}")
	public String deleteBandposition(
			Model model,
			Principal principal,
			@PathVariable(name = "id") Long id, 
			@PathVariable(name = "posID") Long posID) {

		try {
			assertValidUser(principal);	
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		bandPositionsRepository.deleteById(posID);
		return "redirect:/band/" + id + "/edit";
	}

	@GetMapping("{id}/view")
	public String viewBand(
			Model model, 
			Principal principal,
			@PathVariable Long id) {

		Band band = null;
		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = assertValidUser(principal);	
			band = createOrRetrieveBand(id, musicianLoggedIn);
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// add band and current musician to the view model
		model.addAttribute("band", band);
		model.addAttribute("musician", musicianLoggedIn);

		return "Band/banddetails";
	}
// Die untenstehenden Routen hatten keine Auswirkungen während den Tests
	
//	@GetMapping("{id}/uploadimage")
//	public String uploadImageGet(
//			Model model, 
//			Principal principal,
//			@PathVariable Long id) {
//
//		Musician musicianLoggedIn = null;
//
//		try {
//			musicianLoggedIn = assertValidUser(principal);	
//		} catch (Throwable t) {
//			model.addAttribute("message", t.getMessage());
//			return "soundmachineerror";
//		}
//
//		model.addAttribute("bandid", id.toString());
//		model.addAttribute("musician", musicianLoggedIn);
//
//		return "Band/bandupsert";
//	}

//	@PostMapping("{id}/uploadimage")
//	public String uploadImagePost(
//			Model model,
//			Principal principal,
//			@PathVariable Long id, 
//			@RequestParam("imagefile") MultipartFile file) {
//
//		try {
//			assertValidUser(principal);	
//		} catch (Throwable t) {
//			model.addAttribute("message", t.getMessage());
//			return "soundmachineerror";
//		}
//
//		imageService.saveImageFileBand(id, file);
//		return "redirect:/band/" + id + "/view";
//	}

	
	// Via this route, the image can be retrieved for display via an HTML image
	// element <img ...>
	@GetMapping("{id}/bandimage")
	public void renderImageFromDB(
			@PathVariable String id, 
			HttpServletResponse response) throws IOException {

		// retrieve band from DB
		Optional<Band> bandOptional = bandRepository.findById(Long.valueOf(id));

		// if band was found and image exists
		if (bandOptional.isPresent() && bandOptional.get().getImage() != null) {

			// get image from Optional
			Band band = bandOptional.get();

			// set result type to http response
			response.setContentType("image/jpeg");

			// write ByteArray to http response
			InputStream is = new ByteArrayInputStream(band.getImage());
			IOUtils.copy(is, response.getOutputStream());
		}
	}

	private Band createOrRetrieveBand(Long bandid, Musician musicianLoggedIn) {
		Band band = null;

		// if a an existing band shall be updated
		if (bandid != null) {

			// read band from database
			Optional<Band> optionalBand = bandRepository.findById(bandid);

			// if band could not be retrieved
			if (!optionalBand.isPresent()) {
				throw new IllegalArgumentException("Band konnte in Datenbank nicht gefunden werden!");
			}

			// initialize band object with the data read from DB
			band = optionalBand.get();

		} else {
			// new band will be created
			band = new Band();

			// Owner will be the musician logged in
			band.setOwner(musicianLoggedIn);
			band.setAddress(new Address());

		}
		return band;
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

	private void initializeBandFormModel(Model model, Band band, Musician musicianLoggedIn) {
		model.addAttribute("band", band);
		model.addAttribute("allGenres", genreRepository.findAll());
		model.addAttribute("positionStates", PositionState.values());
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("musician", musicianLoggedIn);
	}
}
