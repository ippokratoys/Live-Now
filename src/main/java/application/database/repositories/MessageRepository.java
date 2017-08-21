package application.database.repositories;

import application.database.Message;
import org.springframework.data.repository.CrudRepository;


public interface MessageRepository extends CrudRepository<Message, Integer> {

}
