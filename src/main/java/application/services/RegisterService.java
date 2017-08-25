package application.services;

import application.database.Login;
import application.database.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.database.repositories.LoginRepository;
import application.database.repositories.UserRoleRepository;

import java.util.Map;


@Service
public class RegisterService{

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    public Boolean createLogin(Map<String,String> allParams) throws Exception{
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
        newLogin=new Login();
        newLogin.setUsername(allParams.get("username"));
        newLogin.setEmail(allParams.get("email"));
        newLogin.setName(allParams.get("name"));
        newLogin.setSurname(allParams.get("surname"));
        newLogin.setPassword(allParams.get("password"));
        newLogin.setPhoneNum(allParams.get("telephone"));
        UserRole newUserRole=new UserRole();

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
            newLogin.setIsHost(true);
            newLogin.setIsCustomer(true);
            newLogin.setEnabled(true);
            newUserRole.setRole("host");
            UserRole newUserRole2=new UserRole();
            newUserRole2.setRole("customer");
            newUserRole2.setLogin(newLogin);
            userRoleRepository.save(newUserRole2);
        }
        newUserRole.setLogin(newLogin);
        loginRepository.save(newLogin);
        userRoleRepository.save(newUserRole);
        return true;
    }
}