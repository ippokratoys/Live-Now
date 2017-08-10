package model;

import org.springframework.data.repository.CrudRepository;

import model.BookInfo;


public interface BookInfoRepository extends CrudRepository<BookInfo,Integer> {

}
