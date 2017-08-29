package application.search;

import application.database.Apartment;
import application.database.Availability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.persistence.Query;

/**
 * Created by thanasis on 16/8/2017.
 */
@Service
public class SearchService {

    @Autowired
    EntityManager entityManager;


    public String fixDate(String date){
        String newDate;
        String[] parts=date.split(" ");
        Date tempDate = null;
        tempDate = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(parts[1]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(tempDate);
        int month = cal.get(Calendar.MONTH);
        System.out.println(month == Calendar.FEBRUARY);
        newDate=parts[5]+month+"/"+parts[2];
        return newDate;
    }

// page : current page from the result list (STARTS FROM 1)
    public Result getResultList(Search search,int page){
        String queryStr = search.buildQuery();
        Query query=entityManager.createNativeQuery(queryStr,Apartment.class);
        search.passParameter(queryStr,query);
        List<Apartment> apartmentsResult = query.getResultList();
        System.out.println(apartmentsResult.size());

        String startDate=fixDate(search.getFromDate().toString());
        String endDate=fixDate(search.getToDate().toString());

        //////chech the availability
        String queryStrAvailability="Select * from availability where availability.apartment_apartment_id=:aparId and (:startDate between availability.from_av and availability.to_av) and (:endDate between availability.from_av and availability.to_av)";
        Query queryAvailability=entityManager.createNativeQuery(queryStrAvailability,Availability.class);
        for (Apartment oneApartment :
                apartmentsResult) {
            queryAvailability.setParameter("aparId",oneApartment.getApartmentId());
            queryAvailability.setParameter("startDate",startDate);
            queryAvailability.setParameter("endDate",endDate);
            List<Availability> availabilityResult = queryAvailability.getResultList();
            if(availabilityResult.size()<1){
                apartmentsResult.remove(oneApartment);
                System.out.println("not available");
                System.out.println(oneApartment.toString());
            }else{
                System.out.println("Available");
                System.out.println(oneApartment.toString());
            }
        }
        System.out.println("\nend of results\n");
        Result searchResults = new Result(apartmentsResult,page);
        return searchResults;

    }
}
