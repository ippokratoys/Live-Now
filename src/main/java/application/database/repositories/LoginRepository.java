package application.database.repositories;

import application.database.Login;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface LoginRepository extends CrudRepository<Login, String> {

}
