package application.services;

import application.database.Login;
import application.database.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import application.database.repositories.LoginRepository;
import application.database.repositories.UserRoleRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


@Service
public class RegisterService{

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    FileUploadService fileUploadService;

    public Boolean createLogin(Map<String,String> allParams,MultipartFile image) throws Exception{

//    public Boolean createLogin(Map<String,String> allParams) throws Exception{
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            if (entry.getValue() == null || entry.getValue().equals("")) {
                throw new Exception("Empty Field");
            }
        }
        Login newLogin=loginRepository.findOne(allParams.get("username"));
        if(newLogin!=null){
            throw new Exception("User Exists");
        }
        if(!allParams.get("password").equals(allParams.get("confirm"))){
            throw new Exception("Passwords do not match");
        }
        int flag_both=0;
        newLogin=new Login();
        newLogin.setUsername(allParams.get("username"));
        newLogin.setEmail(allParams.get("email"));
        newLogin.setName(allParams.get("name"));
        newLogin.setSurname(allParams.get("surname"));
        newLogin.setPassword(allParams.get("password"));
        newLogin.setPhoneNum(allParams.get("telephone"));



        UserRole newUserRole=new UserRole();
        UserRole newUserRole2=new UserRole();

        if(allParams.get("user-role").equals("host")){
            newLogin.setIsHost(true);
            newLogin.setIsCustomer(false);
            newLogin.setEnabled(false);
            newUserRole.setRole("host");
        }else if (allParams.get("user-role").equals("customer")){
            newLogin.setIsHost(false);
            newLogin.setIsCustomer(true);
            newLogin.setEnabled(true);
            newUserRole.setRole("customer");
        }else{
            flag_both=1;
            newLogin.setIsHost(true);
            newLogin.setIsCustomer(true);
            newLogin.setEnabled(false);
            newUserRole.setRole("host");
            newUserRole2.setRole("customer");
            newUserRole2.setLogin(newLogin);
        }
        newUserRole.setLogin(newLogin);
        Login dbLogin=loginRepository.save(newLogin);
        userRoleRepository.save(newUserRole);
        if(flag_both==1){
            userRoleRepository.save(newUserRole2);
        }
        String fileName="UsersPhotos/";
        fileName += allParams.get("username");
        String[] buff=image.getOriginalFilename().split("\\.");
        String fileNamePostFix=buff[buff.length-1];
        fileName+="."+fileNamePostFix;
//
        fileUploadService.store(image,fileName);
        dbLogin.setPhotoPath(fileName);
        loginRepository.save(dbLogin);
        return true;
    }

    public void editLogin(String username,String name,String surname,String phone,String email,String password,String confirm,MultipartFile image) throws Exception{
        Login login=loginRepository.findOne(username);
        if(login==null){
            throw new Exception("the login does not exit");
        }
        login.setSurname(surname);
        login.setPhoneNum(phone);
        login.setName(name);
        login.setEmail(email);
        if(!password.equals("")){
            if(!password.equals(confirm)){
                throw new Exception("The password doesnot match with the confirm");
            }
            login.setPassword(password);
        }
        System.out.println(image);
        if(image==null){
            System.out.println("not image given");
        }else{
            Path pathFile = null;
            Resource file = null;
            pathFile= Paths.get("UsersPhotos/");
            try {
                String filename=login.getPhotoPath().replaceAll("UsersPhotos/","");
                file=new UrlResource(pathFile.resolve(filename).toUri());
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
                String fileName="UsersPhotos/";
                fileName += login.getUsername();
                String[] buff=image.getOriginalFilename().split("\\.");
                String fileNamePostFix=buff[buff.length-1];
                fileName+="."+fileNamePostFix;
                fileUploadService.store(image,fileName);
                login.setPhotoPath(fileName);
            }else{
                System.out.println("delete the old");
                File deleteImage;
                deleteImage=file.getFile();
                deleteImage.delete();
                login.setPhotoPath("not");
                String fileName="UsersPhotos/";
                fileName += login.getUsername();
                String[] buff=image.getOriginalFilename().split("\\.");
                String fileNamePostFix=buff[buff.length-1];
                fileName+="."+fileNamePostFix;
                fileUploadService.store(image,fileName);
                login.setPhotoPath(fileName);
            }
        }
        loginRepository.save(login);
    }

    public Boolean isHostEnabled(String username){
        Login login=loginRepository.findOne(username);
        if(login==null){
            return false;
        }
        for (UserRole role : login.getUserRoles()) {
            if (role.getRole().equals("host")){
                //if he is a host what ever the var says
                return login.getEnabled();
            }
        }
        //if not a host
        return false;
    }

    public Boolean isHost(String username){
        Login login=loginRepository.findOne(username);
        if(login==null){
            return false;
        }
        for (UserRole role : login.getUserRoles()) {
            if (role.getRole().equals("host")){
                return true;
            }
        }

        return false;
    }

    public Boolean isUser(String username){
        Login login=loginRepository.findOne(username);
        if(login==null){
            return false;
        }
        for (UserRole role : login.getUserRoles()) {
            if (role.getRole().equals("customer")){
                return true;
            }
        }
        return false;
    }
}