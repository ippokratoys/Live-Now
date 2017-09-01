package application.services;

import application.database.Apartment;
import application.database.Login;
import application.database.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public Boolean authentication(UserDetails userDetails, int apartmentId){

        if(userDetails==null){
            return false;
        }
        Apartment apartment = apartmentRepository.findOne(apartmentId);
        if(apartment==null){
            return false;
        }

        if(apartment.getLogin().getUsername().equals(userDetails.getUsername())){
            return true;
        }else {
            return false;
        }

    }

    public Boolean authentication(UserDetails userDetails,Apartment apartment){
        if(apartment==null){
            return false;
        }
        return authentication(userDetails,apartment.getApartmentId());
    }

    public Boolean authentication(String username,Apartment apartment){
        return true;
    }

    public Boolean authentication(String username,int apartmentId){
        return true;
    }



    public Boolean editApartment(int apartmentId,Apartment newApartment) throws Exception{
        return true;
    }

    public Boolean newMessageFormHost(String message,int chatId){
        System.out.println("must create this :( !!!");
        return true;
    }

    public Boolean newMessageFromUser(String message, String username, int apartmentId){
        return true;
    }
}
