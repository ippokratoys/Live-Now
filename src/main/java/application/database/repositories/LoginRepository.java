package model;

import org.springframework.data.repository.CrudRepository;

import model.Login;


public interface LoginRepository extends CrudRepository<Login, String> {

}
