package application.database.repositories;

import application.database.Apartment;
import application.database.Login;
import application.database.UserRole;
import application.database.UserVector;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import sun.rmi.runtime.Log;

import java.util.List;

/**
 * Created by thanasis on 1/10/2017.
 */

public interface UserVectorRepositorie extends CrudRepository<UserVector, String>{

    public Integer countAllByRoomTypeAndLogin(String roomType, Login login);

    public List<UserVectorRepositorie> findAllByLogin(Login login);

    @Query("SELECT AVG(x.people) FROM UserVector x where x.login=?1 and x.people<>0")
    public Double findAvgMaxPplOfLogin(Login login);

    @Query("SELECT AVG(x.price) FROM UserVector x where x.login=?1 and x.price<>0")
    public Double findAvgMaxPriceOfLogin(Login login);

    @Query("SELECT count(x) FROM UserVector x where x.login=?1 and (" +
            "(x.searchString1 like ?2 OR x.searchString1 like ?3) or" +
            "(x.searchString2 like ?2 OR x.searchString2 like ?3) )")
    public int getTimesLoginSearchedForArea(Login login,String country,String locality);
}
