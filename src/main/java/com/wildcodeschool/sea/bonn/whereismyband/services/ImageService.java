package com.wildcodeschool.sea.bonn.whereismyband.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void saveImageFileBand(Long id, MultipartFile file);
    void saveImageFileMusician(Long id, MultipartFile file);
    public String getImageType(byte[] image);
}
