package application.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {


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

}
