package com.wildcodeschool.sea.bonn.whereismyband.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFile(Long bandId, MultipartFile file);
}
