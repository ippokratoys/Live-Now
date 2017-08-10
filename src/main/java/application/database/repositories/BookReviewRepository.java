package model;

import org.springframework.data.repository.CrudRepository;

import model.BookReview;


public interface BookReviewRepository extends CrudRepository<BookReview, Integer> {

}
