package application.basicControllers;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by thanasis on 21/9/2017.
 */

@Controller
public class FileController {

    @RequestMapping("UsersPhotos/{fileName:.+}")
    ResponseEntity<Resource> getEventFile(@PathVariable String fileName){
        System.out.println("fileName = [" + fileName + "]");
        Path pathFile = null;
        Resource file = null;
        pathFile= Paths.get("UsersPhotos/");
        try {
            file=new UrlResource(pathFile.resolve(fileName).toUri());
            System.out.println(file.getURI());
            if(file.exists() || file.isReadable()) {
//                it's ok
                ;
            }else {
                file=null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            file=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(file==null){
            System.out.println("file is nyll");
            return ResponseEntity
                    .notFound()
                    .build();
        }
        System.out.println("file is good "+file.getFilename());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

    @RequestMapping("ApartmentPhotos/{fileName:.+}")
    ResponseEntity<Resource> getApartmentPhotos(@PathVariable String fileName){
        System.out.println("fileName = [" + fileName + "]");
        Path pathFile = null;
        Resource file = null;
        pathFile= Paths.get("ApartmentPhotos/");
        try {
            file=new UrlResource(pathFile.resolve(fileName).toUri());
            System.out.println(file.getURI());
            if(file.exists() || file.isReadable()) {
//                it's ok
                ;
            }else {
                file=null;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
            file=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(file==null){
            System.out.println("file is nyll");
            return ResponseEntity
                    .notFound()
                    .build();
        }
        System.out.println("file is good "+file.getFilename());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+file.getFilename()+"\"")
                .body(file);
    }

}
