package application.database.repositories;

import application.database.Apartment;
import application.database.BookInfo;
import application.database.BookReview;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BookReviewRepository extends CrudRepository<BookReview, Integer> {
    public BookReview findByBookInfo(BookInfo bookInfo);
    public List<BookReview> findAllByApartment(Apartment apartment);
}
