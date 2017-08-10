package model;

import org.springframework.data.repository.CrudRepository;

import model.Apartment;


public interface ApartmentRepository extends CrudRepository<Apartment,Integer> {

}
