package application.database.repositories;

import application.database.Chat;
import application.database.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface MessageRepository extends CrudRepository<Message, Integer> {
    public List<Message> findAllByChatOrderByMessageId(Chat chat);
}
