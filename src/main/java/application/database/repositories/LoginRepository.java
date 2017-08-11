package application.database.repositories;

import application.database.Login;
import org.springframework.data.repository.CrudRepository;


public interface LoginRepository extends CrudRepository<Login, String> {

}
