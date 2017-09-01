package application.database.repositories;

import application.database.Apartment;
import application.database.Chat;
import application.database.Login;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ChatRepository extends CrudRepository<Chat, Integer> {

    public List<Chat> findAllByApartment(Apartment apartment);
    public List<Chat> findAllByLoginOrderByChatIdDesc(Login login);



}
