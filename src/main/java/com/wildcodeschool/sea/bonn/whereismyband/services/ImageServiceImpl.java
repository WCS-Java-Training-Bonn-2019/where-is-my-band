package com.wildcodeschool.sea.bonn.whereismyband.services;

import java.io.IOException;

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

}
