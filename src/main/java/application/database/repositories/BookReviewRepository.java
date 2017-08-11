package application.database.repositories;

import application.database.BookReview;
import org.springframework.data.repository.CrudRepository;


public interface BookReviewRepository extends CrudRepository<BookReview, Integer> {

}
