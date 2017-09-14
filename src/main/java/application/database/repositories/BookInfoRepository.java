package application.database.repositories;

import application.database.BookInfo;
import application.database.Login;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface BookInfoRepository extends CrudRepository<BookInfo,Integer> {
    public List<BookInfo> findAllByLoginOrderByBookIn(Login login);
}
