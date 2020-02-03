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
            Band band = bandRepository.findById(bandId).get();

            Byte[] byteObjects = new Byte[file.getBytes().length];

            int i = 0;

            for (byte b : file.getBytes()){
                byteObjects[i++] = b;
            }

            band.setImage(byteObjects);

            bandRepository.save(band);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
