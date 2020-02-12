package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
@RequestMapping("/band/{id}")
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

	@GetMapping("upsert")
	public String getUpsertBandForm(Model model, Principal principal,
			@PathVariable(required = false, name = "id") Long bandid) {

		Optional<Musician> userOptional = musicianRepository.findByUsername(principal.getName());

		// musician matching the principal was found in DB
		if (! userOptional.isPresent()) {
			model.addAttribute("message", "Angemeldeter Benutzer wurde nicht in der Datenbank gefunden");
			return "soundmachinerror";
		};

		Musician musicianLoggedIn = userOptional.get();

		Band band = null;
		
		// if a an existing band shall be updated
		if (bandid != null) {

			// read band from database
			Optional<Band> optionalBand = bandRepository.findById(bandid);

			// if band could not be retrieved
			if (! optionalBand.isPresent()) {
				model.addAttribute("message", "Band konnte in Datenbank nicht gefunden werden!");
				return "soundmachinerror";
			};				

			// initialize band object with the data read from DB
			band = optionalBand.get();

		} else {
			// new band will be created
			band = new Band();

			// Owner will be the musician logged in
			band.setOwner(musicianLoggedIn);
			band.setAddress(new Address());

		}

		// add band to the view model (either empty or prefilled with DB data)
		model.addAttribute("band", band);
		model.addAttribute("allGenres", genreRepository.findAll());
		model.addAttribute("positionStates", PositionState.values());
		model.addAttribute("allInstruments", instrumentRepository.findAll());
		model.addAttribute("musician", band.getOwner());

		return "bandupsert";
	}

	@PostMapping("upsert")
	public String postUpsertBandForm(Model model, @ModelAttribute Band band, Principal principal) {

		if (band.getId() != null) {
			// read band as existing in DB
			Band bandFromDB = bandRepository.getOne(band.getId());

			// DB contains an image for this band
			if (bandFromDB.getImage() != null) {
				// set image of band to the one stored n the DB
				band.setImage(bandFromDB.getImage());
			}
		}

		// save band.address in address table
		addressRepository.save(band.getAddress());

		// save band attributes in band table
		bandRepository.save(band);

		// save bandpositions in bandposition table
		bandPositionsRepository.saveAll(band.getBandPositions());

		model.addAttribute(band);

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

			model.addAttribute("musician", musicianOptional.get());
		}

		return "redirect:/band/" + band.getId() + "/view";
	}

	// create a new Position for the band identified by ID
	@PostMapping("newposition")
	public String createNewBandposition(Model model, 
			@PathVariable (required = true, name="id") Long bandID, 
			@RequestParam(required = true, name="instrID") Long instrID) {
		// look for band with id in DB
		Optional<Band> bandOptional = bandRepository.findById(bandID);

		// if band with id could not be found
		if (!bandOptional.isPresent()) {
			model.addAttribute("message", "A band with this ID could not be found!");
			return "soundmachineerror";
		}

		// look for instrument in DB
		Optional<Instrument> instrumentOptional = instrumentRepository.findById(instrID);
		// if instrument could not be found
		if (! instrumentOptional.isPresent()) {
			model.addAttribute("message", " Instrument with this ID could not be found");
			return "soundmachineerror";
		}

		// retrieve band and instrument from DB
		Band band = bandOptional.get();
		Instrument instrument = instrumentOptional.get();		

		// create bandposition with the instrument identified by instrID for band
		Bandposition bandPosition = new Bandposition();
		bandPosition.setInstrument(instrument);
		bandPosition.setBand(band);
		bandPosition.setState(PositionState.offen);
		bandPosition.setAgeFrom(20);
		bandPosition.setAgeTo(70);
		bandPositionsRepository.save(bandPosition);

		if (band.getId() != null) {
			return "redirect:/band/" + band.getId() + "/upsert";
		} else {
			return "redirect:/band//upsert";
		}
	}


	// KrillMi, 10.02.2020: Deletes the position identified by posID for the band identified by id
	@GetMapping("delete/{posID}")
	public String deleteBand(@PathVariable(name="id") Long id, @PathVariable(name="posID") Long posID) {
		bandPositionsRepository.deleteById(posID);
		return "redirect:/band/" + id + "/edit";
	}

	@GetMapping("view")
	public String viewBand(Model model,
			@PathVariable Long id, Principal principal) {

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

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

			model.addAttribute("musician", musicianOptional.get());
		}

		return "banddetails";
	}

	@GetMapping("uploadimage")
	public String showUploadForm(@PathVariable String id, Model model, Principal principal){
		model.addAttribute("bandid", id);

		if (principal != null) {
			Optional<Musician> musicianOptional = musicianRepository.findByUsername(principal.getName());

			model.addAttribute("musician", musicianOptional.get());
		}

		return "imageuploadform";
	}

	@PostMapping("uploadimage")
	public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){

		imageService.saveImageFileBand(Long.valueOf(id), file);
		return "redirect:/band/" + id + "/view";
	}

	// Via this route, the image can be retrieved for display via an HTML image element <img ...>
	@GetMapping("bandimage")
	public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {

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

}
