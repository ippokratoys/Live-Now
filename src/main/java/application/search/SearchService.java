package application.search;

import application.database.Apartment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Iterator;
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
        List<Object> apartments = (List<Object>) query.getResultList();
        Iterator itr = apartments.iterator();
        while(itr.hasNext()){
            Object[] obj = (Object[]) itr.next();
            //now you have one array of Object for each row
            Integer apartment_id = Integer.parseInt(String.valueOf(obj[0]));
            System.out.println(apartment_id);
//            String client = String.valueOf(obj[0]);
        }
//        System.out.println(apartments.get(0));
//        for (String oneApartment : apartments){
//            System.out.println(oneApartment);
//            oneApartment.toString();
//            System.out.printf("-------------------------------\n");
//        }
        return null;
    }
}
