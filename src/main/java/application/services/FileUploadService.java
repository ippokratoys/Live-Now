package application.services;

import application.database.Apartment;
import application.database.Image;
import application.database.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {

    @Autowired
    ImageRepository imageRepository;

    Path image;

    public void store(MultipartFile file, String fileName) throws Exception {
        if (file.isEmpty()) {
                throw new Exception("empty file ");
        }
        byte[] bytes = file.getBytes();
        Path path = Paths.get(fileName);
        Files.write(path, bytes);

//        Files.copy(file.getInputStream(), this.image.resolve(fileName));
    }

    public void save_image(MultipartFile image,Apartment apartment) throws Exception {
        if (image!=null){
            Image newImage=new Image();
            newImage.setApartment(apartment);
            Image Newimage2=imageRepository.save(newImage);
            String fileName="ApartmentPhotos/";
            fileName+=apartment.getLogin().getName()+"-";
            fileName+=apartment.getApartmentId()+"-";
            fileName+=Newimage2.getImageId();
            String[] buff=image.getOriginalFilename().split("\\.");
            String fileNamePostFix=buff[buff.length-1];
            fileName+="."+fileNamePostFix;
            store(image,fileName);
            Newimage2.setPicturePath(fileName);
            imageRepository.save(Newimage2);
        }
    }

}
