package application.search;

import application.database.Apartment;

import com.sun.org.apache.regexp.internal.RE;
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

// page : current page from the result list (STARTS FROM 1)
    public Result getResultList(Search search,int page){
        String queryStr = search.buildQuery();
        Query query=entityManager.createNativeQuery(queryStr,Apartment.class);
        search.passParameter(queryStr,query);
        List<Apartment> apartmentsResult = query.getResultList();
        System.out.println("\nstart printing results\n");
        System.out.println(apartmentsResult.size());
        for (Apartment oneApartment :
                apartmentsResult) {
            System.out.println(oneApartment.toString());
        }
        System.out.println("\nend of results\n");

        Result searchResults = new Result(apartmentsResult);
        return searchResults;

    }
}
