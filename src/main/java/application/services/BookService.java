package application.services;


import application.database.Apartment;
import application.database.BookInfo;
import application.database.Login;
import application.database.UserRole;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.AvailabilityRepository;
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
    AvailabilityService availabilityService;

    @Autowired
    BookInfoRepository bookInfoRepository;

    public boolean createBookInfo(int apartmentId,String username,String book_in,String book_out)throws Exception{
        Apartment apartment=apartmentRepository.findOne(apartmentId);
        Login login=loginRepository.findOne(username);
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
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        if(availabilityService.checkAvailability(apartment,format.parse(book_in),format.parse(book_out))!=true) {
            throw new Exception("The apartment is not available");
        }
        BookInfo newBookInfo=new BookInfo();
        newBookInfo.setLogin(login);
        newBookInfo.setApartment(apartment);
        newBookInfo.setBookIn(format.parse(book_in));
        newBookInfo.setBookOut(format.parse(book_out));
        bookInfoRepository.save(newBookInfo);
        return true;
    }
}
