package application.database.repositories;

import application.database.CustomerInfo;
import org.springframework.data.repository.CrudRepository;




public interface CustomerInfoRepository extends CrudRepository<CustomerInfo, String> {

}
