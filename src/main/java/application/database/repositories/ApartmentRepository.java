package application.database.repositories;

import application.database.Apartment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ApartmentRepository extends CrudRepository<Apartment,Integer> {
    public List<Apartment> findAllByOrderByApartmentId();
}
