package application.services;

import application.database.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.database.repositories.LoginRepository;

import java.util.Map;


@Service
public class RegisterService{

    @Autowired
    LoginRepository loginRepository;

    public Boolean createLogin(Map<String,String> allParams) throws Exception{
        for (Map.Entry<String, String> entry : allParams.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            if (entry.getValue() == null || entry.getValue() == "") {
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
        return true;
    }
}