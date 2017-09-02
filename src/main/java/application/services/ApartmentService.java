package application.services;

import application.database.*;
import application.database.repositories.ChatRepository;
import application.database.repositories.LoginRepository;
import application.database.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import application.database.repositories.ApartmentRepository;

import java.util.Date;
import java.util.List;

@Service
public class ApartmentService{

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

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
        Apartment apartment=apartmentRepository.findOne(apartmentId);
        apartment.setAircondition(newApartment.getAircondition());
        apartment.setArea(newApartment.getArea());
        apartment.setBaths(newApartment.getBaths());
        apartment.setBed(newApartment.getBed());
        apartment.setCleanPrice(newApartment.getCleanPrice());
        apartment.setEvents(newApartment.getEvents());
        apartment.setFridge(newApartment.isFridge());
        apartment.setGarage(newApartment.getGarage());
        apartment.setHeat(newApartment.getHeat());
        apartment.setHouseDescription(newApartment.getHouseDescription());
        apartment.setKitchen(newApartment.getKitchen());
        apartment.setLat(newApartment.getLat());
        apartment.setLeavingRoom(newApartment.getLeavingRoom());
        apartment.setLift(newApartment.getLift());
        apartment.setLocation(newApartment.getLocation());
        apartment.setLon(newApartment.getLon());
        apartment.setMaxPeople(newApartment.getMaxPeople());
        apartment.setMinPeople(newApartment.getMinPeople());
        apartment.setName(newApartment.getName());
        apartment.setParking(newApartment.getParking());
        apartment.setPets(newApartment.getPets());
        apartment.setPlusPrice(newApartment.getPlusPrice());
        apartment.setPrice(newApartment.getPrice());
        apartment.setRooms(newApartment.getRooms());
        apartment.setRules(newApartment.getRules());
        apartment.setSmoking(newApartment.getSmoking());
        apartment.setStandardPeople(newApartment.getStandardPeople());
        apartment.setTrasnportationDescription(newApartment.getTrasnportationDescription());
        apartment.setTv(newApartment.getTv());
        apartment.setType(newApartment.getType());
        apartment.setWi_fi(newApartment.getWi_fi());
        apartmentRepository.save(apartment);
        return true;
    }

    public Boolean newMessageFormHost(String message,int chatId){
        Chat chat=chatRepository.findOne(chatId);
        Message newMessage= new Message();
        newMessage.setChat(chat);
        newMessage.setContent(message);
        Date date = new Date();
        newMessage.setDateTime(date);
        messageRepository.save(newMessage);
        return true;
    }

    public Boolean newMessageFromUser(String message, String username, int apartmentId){
        int notCreateNew=0;
        Chat createdChat=null;
        Message newMessage=new Message();
        Apartment apartment=apartmentRepository.findOne(apartmentId);
        List<Chat> chatlist=chatRepository.findAllByApartment(apartment);
        for(Chat chat : chatlist){
            if(chat.getLogin().getUsername().equals(username)){
               notCreateNew=1;
               createdChat=chat;
               break;
            }
        }
        if(notCreateNew==1){
            newMessage.setChat(createdChat);
            newMessage.setContent(message);
            Date date = new Date();
            newMessage.setDateTime(date);
            messageRepository.save(newMessage);
        }else{
            Chat newChat=new Chat();
            newChat.setApartment(apartment);
            newChat.setLogin(loginRepository.findOne(username));
            createdChat=chatRepository.save(newChat);
            newMessage.setChat(createdChat);
            newMessage.setContent(message);
            Date date = new Date();
            newMessage.setDateTime(date);
            messageRepository.save(newMessage);
        }
        return true;
    }
}
