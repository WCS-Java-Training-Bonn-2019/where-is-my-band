package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
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

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Genre;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.entity.PositionState;
import com.wildcodeschool.sea.bonn.whereismyband.repository.AddressRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandpositionRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.GenreRepository;
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
	
	@Autowired
	public BandController(BandRepository bandRepository, MusicianRepository musicianRepository,
			GenreRepository genreRepository, AddressRepository addressRepository,
			BandpositionRepository bandPositionsRepository, ImageService imageService) {
		super();
		this.bandRepository = bandRepository;
		this.musicianRepository = musicianRepository;
		this.genreRepository = genreRepository;
		this.addressRepository = addressRepository;
		this.bandPositionsRepository = bandPositionsRepository;
		this.imageService = imageService;
	}

	@GetMapping("list")
	public String getAll(Model model) {
		model.addAttribute("bands", bandRepository.findAll());
		return "bands";
	}

	@GetMapping("edit")
	public String getBand(Model model,
			@PathVariable(name = "id") Long bandid,
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
			Musician owner = musicianRepository.findByFirstNameAndLastName("Elke", "E-Gitarre").get(0);
			
			// if musician with ownerid given was found in DB
			if (owner != null) {
				// initialize band.owner
				band.setOwner(owner);
			}
		}

		// add band to the view model (either empty or prefilled with DB data)
		model.addAttribute("band", band);
		model.addAttribute("allGenres", new HashSet<Genre>(genreRepository.findAll()));
		model.addAttribute("positionStates", PositionState.values());

		return "bandupsert";
	}

	@PostMapping("edit")
	public String postBand(Model model, @ModelAttribute Band band) {

		// read band as existing in DB
		Band bandFromDB = bandRepository.getOne(band.getId());
		
		// DB contains an image for this band
		if (bandFromDB.getImage() != null) {
			// set image of band to the one stored n the DB
			band.setImage(bandFromDB.getImage());
		}
		
		// save band.address in address table
		addressRepository.save(band.getAddress());
		
		// save band attributes in band table
		bandRepository.save(band);

		// save bandpositions in bandposition table
		bandPositionsRepository.saveAll(band.getBandPositions());
		
		model.addAttribute(band);
		return "redirect:/band/" + band.getId() + "/view";
	}

	@GetMapping("delete")
	public String deleteBand(@PathVariable Long id) {
		bandRepository.deleteById(id);
		return "redirect:list";
	}

	@GetMapping("view")
	public String viewBand(Model model,
			@PathVariable Long id) {

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
	
	@GetMapping("uploadimage")
	public String showUploadForm(@PathVariable String id, Model model){
		model.addAttribute("bandid", id);

		return "imageuploadform";
	}

	@PostMapping("uploadimage")
	public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){

		imageService.saveImageFile(Long.valueOf(id), file);
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
			
			// write bytes of image to byteArray
			byte[] byteArray = new byte[band.getImage().length];
			int i = 0;

			for (Byte wrappedByte : band.getImage()){
				byteArray[i++] = wrappedByte; //auto unboxing
			}

			// set result type to http response
			response.setContentType("image/jpeg");
			
			// write ByteArray to http response
			InputStream is = new ByteArrayInputStream(byteArray);
			IOUtils.copy(is, response.getOutputStream());
		}
	}

}
