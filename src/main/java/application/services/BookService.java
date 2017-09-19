package application.services;


import application.database.*;
import application.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @Autowired
    BookReviewRepository bookReviewRepository;

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
        DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        if(availabilityService.checkAvailability(apartment,format.parse(book_in),format.parse(book_out))!=true) {
            throw new Exception("The apartment is not available");
        }
        BookInfo newBookInfo=new BookInfo();
        newBookInfo.setLogin(login);
        newBookInfo.setApartment(apartment);
        newBookInfo.setReviewDone(false);
        newBookInfo.setBookIn(format.parse(book_in));
        newBookInfo.setBookOut(format.parse(book_out));
        bookInfoRepository.save(newBookInfo);
        return true;
    }

    public boolean createRating(int bookId,short rating,String content)throws Exception{
        BookInfo bookInfo=bookInfoRepository.findOne(bookId);
        BookReview oldBookReview=bookReviewRepository.findByBookInfo(bookInfo);
        if(oldBookReview!=null){
           throw new Exception("There is Review");
        }
        BookReview bookReview;
        bookReview=new BookReview();
        bookReview.setRating(rating);
        bookReview.setComment(content);
        bookReview.setBookInfo(bookInfo);
        bookReview.setApartment(bookInfo.getApartment());
        Date date = new Date();
        bookReview.setTime(date);
        bookInfo.setReviewDone(true);
        bookInfoRepository.save(bookInfo);
        bookReviewRepository.save(bookReview);
        return true;
    }
}
