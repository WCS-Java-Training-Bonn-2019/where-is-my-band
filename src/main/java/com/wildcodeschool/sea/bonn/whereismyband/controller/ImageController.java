package com.wildcodeschool.sea.bonn.whereismyband.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.services.ImageService;


@Controller
public class ImageController {

    private final ImageService imageService;
    private final BandRepository bandRepository;

	@Autowired
	public ImageController(ImageService imageService, BandRepository bandRepository) {
		super();
		this.imageService = imageService;
		this.bandRepository = bandRepository;
		
		
	}

    @GetMapping("band/{id}/changeimage")
    public String showUploadForm(@PathVariable String id, Model model){
        model.addAttribute("band", bandRepository.findById(Long.valueOf(id)).get());

        return "imageuploadform";
    }

    @PostMapping("band/{id}/changeimage")
    public String handleImagePost(@PathVariable String id, @RequestParam("imagefile") MultipartFile file){

        imageService.saveImageFile(Long.valueOf(id), file);

        return "redirect:/band/" + id + "/view";
    }

	   @GetMapping("band/{id}/bandimage")
	    public void renderImageFromDB(@PathVariable String id, HttpServletResponse response) throws IOException {
	        Optional<Band> bandOptional = bandRepository.findById(Long.valueOf(id));

	        if (bandOptional.isPresent() && bandOptional.get().getImage() != null) {
	            Band band = bandOptional.get();
	            byte[] byteArray = new byte[band.getImage().length];
	            int i = 0;

	            for (Byte wrappedByte : band.getImage()){
	                byteArray[i++] = wrappedByte; //auto unboxing
	            }

	            response.setContentType("image/jpeg");
	            InputStream is = new ByteArrayInputStream(byteArray);
	            IOUtils.copy(is, response.getOutputStream());
	        }
	    }
}
