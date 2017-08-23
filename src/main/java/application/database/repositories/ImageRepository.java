package application.database.repositories;

import application.database.Image;
import org.springframework.data.repository.CrudRepository;


public interface ImageRepository extends CrudRepository<Image,Integer> {

}
