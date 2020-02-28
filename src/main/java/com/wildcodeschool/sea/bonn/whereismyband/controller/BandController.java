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
import org.springframework.data.domain.Sort;
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
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);	
			band = createOrRetrieveBand(bandid, musicianLoggedIn);
		} catch (Throwable t) {
			// if musician or band could not be read from DB
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// if the logged-in musician is not the band owner
		if (!band.getOwner().equals(musicianLoggedIn)) {
			model.addAttribute("musician", musicianLoggedIn);
			model.addAttribute("message", "Sie können nur Ihre eigenen Bands bearbeiten.");
			return "soundmachineerror";
		}

		addBandAndMusicianToViewModel(model, band, musicianLoggedIn);
		return "Band/bandupsert";
	}

	@PostMapping("{id}/edit")
	public String editBandPost(
			Model model,
			Principal principal,
			@Valid @ModelAttribute Band band, 
			BindingResult bindingResult,
			@PathVariable(name="id") Long bandId) {

		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);
		} catch (Throwable t) {
			// if musician or band could not be read from DB
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// if the logged-in musician is not the band owner
		if (!band.getOwner().equals(musicianLoggedIn)) {
			model.addAttribute("musician", musicianLoggedIn);
			model.addAttribute("message", "Sie können nur Ihre eigenen Bands bearbeiten.");
			return "soundmachineerror";
		}

		if(bindingResult.hasErrors()) {
			addBandAndMusicianToViewModel(model, band, musicianLoggedIn);
			return "Band/bandupsert";
		}

		// if image wasn't updated via Form
		if (band.getImage().length == 0) {
			// set image of band to the one stored in the DB
			Band bandFromDB = bandRepository.getOne(band.getId());
			if (bandFromDB == null) {
				model.addAttribute("musician", musicianLoggedIn);
				model.addAttribute("message", "Die Band mit der ID" + band.getId() + " existiert nicht in der Datenbank!");
				return "soundmachineerror";
			}

			band.setImage(bandFromDB.getImage());

		}

		// save all entities in DB
		addressRepository.save(band.getAddress());
		bandPositionsRepository.saveAll(band.getBandPositions());
		bandRepository.save(band);

		return "redirect:/band/" + band.getId() + "/view";
	}

	@GetMapping("new")
	public String newBandGet(
			Model model, 
			Principal principal) {

		Band band = null;
		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);	
			band = createOrRetrieveBand(null, musicianLoggedIn);
		} catch (Throwable t) {
			// if musician or band could not be read from DB
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// add band to the view model (either empty)
		addBandAndMusicianToViewModel(model, band, musicianLoggedIn);
		return "Band/bandupsert";
	}


	@PostMapping("new")
	public String newBandPost(
			Model model,
			Principal principal,
			@ModelAttribute Band band) {

		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);	
		} catch (Throwable t) {
			// if musician could not be read from DB
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// set owner to the musician currently logged in
		band.setOwner(musicianLoggedIn);

		// save entities in DB
		addressRepository.save(band.getAddress());
		bandPositionsRepository.saveAll(band.getBandPositions());
		bandRepository.save(band);

		addBandAndMusicianToViewModel(model, band, musicianLoggedIn);

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
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);	
			band = createOrRetrieveBand(bandID, musicianLoggedIn);
		} catch (Throwable t) {
			// if musician or band could not be read from DB
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
		bandPositionsRepository.save(bandPosition);

		return "redirect:/band/" + bandID + "/edit";
	}

	// KrillMi, 10.02.2020: Deletes the position identified by posID for the band
	// identified by id
	@GetMapping("{id}/deletebandposition/{posID}")
	public String deleteBandposition(
			Model model,
			Principal principal,
			@PathVariable(name = "id") Long bandId, 
			@PathVariable(name = "posID") Long posID) {

		try {
			getMusicianLoggedInFromDB(principal);	
		} catch (Throwable t) {
			// if musician could not be read from DB
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		bandPositionsRepository.deleteById(posID);
		return "redirect:/band/" + bandId + "/edit";
	}

	@GetMapping("{id}/view")
	public String viewBand(
			Model model, 
			Principal principal,
			@PathVariable(name = "id") Long id) {

		Band band = null;
		Musician musicianLoggedIn = null;

		try {
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);	
			band = createOrRetrieveBand(id, musicianLoggedIn);
		} catch (Throwable t) {
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		addBandAndMusicianToViewModel(model, band, musicianLoggedIn);

		return "Band/banddetails";
	}


	@GetMapping("{id}/uploadimage")
	public String uploadImageGet(
			Model model, 
			Principal principal,
			@PathVariable(name = "id") Long bandId) {

		Band bandFromDB = null;
		Musician musicianLoggedIn = null;

		try {
			bandFromDB = createOrRetrieveBand(bandId, musicianLoggedIn);
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);	
		} catch (Throwable t) {
			// if musician or band could not be read from DB
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// if the logged-in musician is not the band owner
		if (!bandFromDB.getOwner().equals(musicianLoggedIn)) {
			model.addAttribute("musician", musicianLoggedIn);
			model.addAttribute("message", "Sie können nur Ihre eigenen Bands bearbeiten.");
			return "soundmachineerror";
		}

		model.addAttribute("bandid", bandId.toString());
		model.addAttribute("musician", musicianLoggedIn);

		return "Band/bandupsert";
	}

	@PostMapping("{id}/uploadimage")
	public String uploadImagePost(
			Model model,
			Principal principal,
			@PathVariable(name = "id") Long bandId, 
			@RequestParam("imagefile") MultipartFile file) {


		Band bandFromDB = null;
		Musician musicianLoggedIn = null;

		try {
			bandFromDB = createOrRetrieveBand(bandId, musicianLoggedIn);
			musicianLoggedIn = getMusicianLoggedInFromDB(principal);	
		} catch (Throwable t) {
			// if musician could not be read from DB
			model.addAttribute("message", t.getMessage());
			return "soundmachineerror";
		}

		// if the logged-in musician is not the band owner
		if (!bandFromDB.getOwner().equals(musicianLoggedIn)) {
			model.addAttribute("musician", musicianLoggedIn);
			model.addAttribute("message", "Sie können nur Ihre eigenen Bands bearbeiten.");
			return "soundmachineerror";
		}

		// save the image only, if one has been uploaded
		if (!file.isEmpty()) {
			imageService.saveImageFileBand(bandId, file);
		}
		imageService.saveImageFileBand(bandId, file);
		return "redirect:/band/" + bandId + "/view";
	}


	// Via this route, the image can be retrieved for display via an HTML image
	// element <img ...>
	@GetMapping("{id}/bandimage")
	public void renderImageFromDB(
			@PathVariable(name="id") String bandId, 
			HttpServletResponse response) throws IOException {

		// retrieve band from DB
		Optional<Band> bandOptional = bandRepository.findById(Long.valueOf(bandId));

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

	/**
	 * Creates a band in bandRepository with the owner passed, if bandId was not passed. 
	 * Otherwise the band is read from bandRepository.
	 * @param bandId Band to be read
	 * @param owner owner to be set for a newly created band
	 * @return Band object (created or read from DB)
	 */
	private Band createOrRetrieveBand(Long bandId, Musician owner) {
		Band band = null;

		// if a an existing band shall be updated
		if (bandId != null) {

			// read band from database
			Optional<Band> optionalBand = bandRepository.findById(bandId);

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
			band.setOwner(owner);
			band.setAddress(new Address());

		}
		return band;
	}

	/**
	 * Reads the musician from musicianRepository, which matches the principal passed.
	 * @param principal
	 * @return Musician object retrieved from DB, "null" if not found
	 */
	private Musician getMusicianLoggedInFromDB(Principal principal) {
		Optional<Musician> userOptional = musicianRepository.findByUsernameIgnoreCase(principal.getName());

		// musician matching the principal was found in DB
		if (!userOptional.isPresent()) {
			throw new IllegalArgumentException("Angemeldeter Benutzer wurde nicht in der Datenbank gefunden");
		}

		Musician musicianLoggedIn = userOptional.get();
		return musicianLoggedIn;
	}

	private void addBandAndMusicianToViewModel(Model model, Band band, Musician musicianLoggedIn) {
		model.addAttribute("band", band);
		model.addAttribute("allGenres", genreRepository.findAll(Sort.by("name")));
		model.addAttribute("positionStates", PositionState.values());
		model.addAttribute("allInstruments", instrumentRepository.findAll(Sort.by("name")));
		model.addAttribute("musician", musicianLoggedIn);
	}

}
