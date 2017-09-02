package application.services;

import application.database.Apartment;
import application.database.BookInfo;
import application.database.BookReview;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.BookInfoRepository;
import application.database.repositories.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class ReviewService {

    @Autowired
    BookInfoRepository bookInfoRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    BookReviewRepository bookReviewRepository;

    public void createBookReview(int bookId,String comment,short rating,int apartmentId) throws Exception{
        BookInfo book=bookInfoRepository.findOne(bookId);
        if(book==null){
            throw new Exception("There is not a book");
        }
        Apartment apartment=apartmentRepository.findOne(apartmentId);
        if(apartment==null){
            throw new Exception("The apartment does not exist");
        }
        BookReview newBookReview=new BookReview();
        newBookReview.setApartment(apartment);
        newBookReview.setBookInfo(book);
        newBookReview.setComment(comment);
        newBookReview.setRating(rating);
        Date date=new Date();
        newBookReview.setTime(date);
        bookReviewRepository.save(newBookReview);
    }

    public  void createHostReview()throws Exception{
        
    }

}
