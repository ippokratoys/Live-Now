package application.database.repositories;

import application.database.BookInfo;
import org.springframework.data.repository.CrudRepository;


public interface BookInfoRepository extends CrudRepository<BookInfo,Integer> {

}
