package application.database.repositories;

import application.database.Apartment;
import org.springframework.data.repository.CrudRepository;


public interface ApartmentRepository extends CrudRepository<Apartment,Integer> {

}
