package application.database.repositories;

import application.database.Chat;
import org.springframework.data.repository.CrudRepository;


public interface ChatRepository extends CrudRepository<Chat, Integer> {

}
