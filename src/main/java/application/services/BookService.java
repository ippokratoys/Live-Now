package application.services;


import application.database.Apartment;
import application.database.BookInfo;
import application.database.Login;
import application.database.UserRole;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.BookInfoRepository;
import application.database.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class BookService {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    BookInfoRepository bookInfoRepository;

    public boolean createBookInfo(Map<String,String> allParams)throws Exception{
        Apartment apartment=apartmentRepository.findOne(Integer.parseInt(allParams.get("apartment_id")));
        Login login=loginRepository.findOne(allParams.get("username"));
        if(apartment==null || login==null){
            throw new Exception("The apartment or the user does not exist");
        }
        List<UserRole> userRoles=login.getUserRoles();
        int is_customer=0;
        for(int i=0;i<userRoles.size();i++){
            if(userRoles.get(i).getRole().equals("customer")){
                is_customer=1;
            }
        }
        if(is_customer==0){
            throw new Exception("User is not a customer");
        }
        if(apartment.getLogin().getUsername().equals(login.getUsername())){
            throw new Exception("The apartment is his");
        }
        BookInfo newBookInfo=new BookInfo();
        newBookInfo.setLogin(login);
        newBookInfo.setApartment(apartment);
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        newBookInfo.setBookIn(format.parse(allParams.get("book_in")));
        newBookInfo.setBookOut(format.parse(allParams.get("book_out")));
        bookInfoRepository.save(newBookInfo);
        return true;
    }
}
