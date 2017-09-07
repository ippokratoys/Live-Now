package application.services;

import application.database.Login;
import application.database.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.database.repositories.LoginRepository;
import application.database.repositories.UserRoleRepository;
import org.springframework.web.multipart.MultipartFile;

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
            newLogin.setEnabled(true);
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
            newLogin.setEnabled(true);
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
        loginRepository.save(login);
    }
}