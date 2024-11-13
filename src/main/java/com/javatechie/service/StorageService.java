package com.javatechie.service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.javatechie.entity.ImageData;
import com.javatechie.respository.StorageRepository;
import com.javatechie.util.ImageUtils;

@Service
public class StorageService {

    @Autowired
    private StorageRepository repository;

    public String uploadImage(MultipartFile file) {
        try {
            ImageData imageData = new ImageData();
            imageData.setName(file.getOriginalFilename());
            imageData.setType(file.getContentType());
            imageData.setImageData(ImageUtils.compressImage(file.getBytes()));

            ImageData savedImage = repository.save(imageData);
            if (savedImage != null) {
                return "file uploaded successfully: " + file.getOriginalFilename();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "file upload failed";
    }

    public byte[] downloadImage(String fileName) {
        Optional<ImageData> dbImageData = repository.findByName(fileName);
        return dbImageData.map(imageData -> ImageUtils.decompressImage(imageData.getImageData()))
                          .orElse(null);
    }
}
