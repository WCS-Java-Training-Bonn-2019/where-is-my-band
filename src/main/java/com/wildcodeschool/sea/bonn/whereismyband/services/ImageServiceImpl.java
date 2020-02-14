package com.wildcodeschool.sea.bonn.whereismyband.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.entity.Musician;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;
import com.wildcodeschool.sea.bonn.whereismyband.repository.MusicianRepository;


@Service
public class ImageServiceImpl implements ImageService {


	private final BandRepository bandRepository;
	private final MusicianRepository musicianRepository;


	public ImageServiceImpl(BandRepository bandRepository, MusicianRepository musicianRepository) {
		super();
		this.bandRepository = bandRepository;
		this.musicianRepository = musicianRepository;
	}


	@Override
	@Transactional
	public void saveImageFileBand(Long id, MultipartFile file) {

		try {
			// retrieve the band, which the image belongs to
			Band band = bandRepository.findById(id).get();

			// set image attribute of band to byteArray 
			band.setImage(file.getBytes());

			// save the band with the image in the DB
			bandRepository.save(band);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public void saveImageFileMusician(Long id, MultipartFile file) {

		try {
			// retrieve the band, which the image belongs to
			Musician musician = musicianRepository.findById(id).get();

			// set image attribute of band to byteArray 
			musician.setImage(file.getBytes());

			// save the band with the image in the DB
			musicianRepository.save(musician);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	// KrillMi, 08.02.2020 => detects the type of image passed as a parameter
	// returns empty String, if nothing was detected
	@Override
	public String getImageType(byte[] image) {

		String imageType = "";

		try {
			
			// Create an InputStream from image byte array
			InputStream is = new ByteArrayInputStream(image);
			
			// Create ImageInputStream
			ImageInputStream iis = ImageIO.createImageInputStream(is);

			// if not image input Stream could be created
			if (iis == null) {
				return null;
			}

			// get all currently registered readers that recognize the image format
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);

			// if not at least one reader was found 
			// [probably not necessary due to iis being null before in this case]
			if (!iter.hasNext()) {
				return null;
			};

			// get the first reader
			ImageReader reader = iter.next();

			// set result variable
			imageType = reader.getFormatName();

			// close stream
			iis.close();


		} catch (IOException e) {
			e.printStackTrace();
		}

		return imageType;
	}


}
