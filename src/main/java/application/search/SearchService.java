package application.search;

import application.database.Apartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import javax.persistence.Query;

/**
 * Created by thanasis on 16/8/2017.
 */
@Service
public class SearchService {

    @Autowired
    EntityManager entityManager;

    public Iterable<Apartment> getResultList(Search search){
        String queryStr = search.buildQuery();
        Query query=entityManager.createNativeQuery(queryStr);
        search.passParameter(queryStr,query);
        List<Apartment> apartments = query.getResultList();
        for (Apartment oneApartment : apartments){
            oneApartment.toString();
            System.out.printf("-------------------------------\n");
        }
        return null;
    }
}
