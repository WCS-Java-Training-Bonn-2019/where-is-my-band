package com.wildcodeschool.sea.bonn.whereismyband.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.wildcodeschool.sea.bonn.whereismyband.entity.Band;
import com.wildcodeschool.sea.bonn.whereismyband.repository.BandRepository;


@Service
public class ImageServiceImpl implements ImageService {


    private final BandRepository bandRepository;

    public ImageServiceImpl( BandRepository bandService) {

        this.bandRepository = bandService;
    }

    @Override
    @Transactional
    public void saveImageFile(Long bandId, MultipartFile file) {

        try {
            // retrieve the band, which the image belongs to
        	Band band = bandRepository.findById(bandId).get();

            // declare a byteArray, whhich hast the same size as the image
        	Byte[] byteArray = new Byte[file.getBytes().length];

            int i = 0;

            // copy the bytes of the file received to byteArray
            for (byte b : file.getBytes()){
                byteArray[i++] = b;
            }

            // set image attribute of band to byteArray 
            band.setImage(byteArray);

            // save the band with the image in the DB
            bandRepository.save(band);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
