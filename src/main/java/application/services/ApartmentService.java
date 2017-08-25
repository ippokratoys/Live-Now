package application.services;

import application.database.Apartment;
import application.database.Login;
import application.database.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import application.database.repositories.ApartmentRepository;

import java.util.List;

@Service
public class ApartmentService{

    @Autowired
    ApartmentRepository apartmentRepository;

    public Boolean createApartment(Login login, Apartment apartment) throws Exception{
        List<UserRole> listUserRole=  login.getUserRoles();
        int isHost=0;
        for(int i=0;i<listUserRole.size();i++){
            if(listUserRole.get(i).getRole().equals("host")){
                isHost=1;
                break;
            }
        }
        if(isHost==0){
            throw new Exception("User is not a Host");
        }
        apartment.setLogin(login);
        apartmentRepository.save(apartment);
        return true;
    }

}