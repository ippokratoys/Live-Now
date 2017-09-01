package application.services;


import application.database.Apartment;
import application.database.Availability;
import application.database.BookInfo;
import application.database.repositories.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;


@Service
public class AvailabilityService{

    @Autowired
    EntityManager entityManager;

    @Autowired
    AvailabilityRepository availabilityRepository;

    public Boolean createAvailability(Apartment apartment, Availability availability)throws Exception{
        if(availability.getFromAv().after(availability.getToAv())){
            throw new Exception("The from is after the to");
        }
        String queryStr = "SELECT * FROM availability WHERE availability.apartment_apartment_id=:aparId and (:myDate1 between availability.from_av and availability.to_av or :myDate2 between availability.from_av and availability.to_av)";
        Query query=entityManager.createNativeQuery(queryStr,Availability.class);
        query.setParameter("aparId",apartment.getApartmentId());
        query.setParameter("myDate1",availability.getFromAv());
        query.setParameter("myDate2",availability.getToAv());
        List<Availability> availabilityResult = query.getResultList();
        if(availabilityResult.size()>0){
            throw new Exception("One of the dates is included or both");
        }
        Availability newavailability=new Availability();
        newavailability.setApartment(apartment);
        newavailability.setFromAv(availability.getFromAv());
        newavailability.setToAv(availability.getToAv());
        availabilityRepository.save(newavailability);
        return true;
    }

    public Boolean checkAvailability(Apartment apartment,Date startDate,Date endDate){
        String queryStrBook="Select * from book_info where book_info.apartment=:aparId and (:startDate between book_info.book_in and book_info.book_out) and (:endDate between book_info.book_in and book_info.book_out)";
        Query queryBook=entityManager.createNativeQuery(queryStrBook, BookInfo.class);

        String queryStrAvailability="Select * from availability where availability.apartment_apartment_id=:aparId and (:startDate between availability.from_av and availability.to_av) and (:endDate between availability.from_av and availability.to_av)";
        Query queryAvailability=entityManager.createNativeQuery(queryStrAvailability,Availability.class);

        queryAvailability.setParameter("aparId",apartment.getApartmentId());
        queryAvailability.setParameter("startDate",startDate);
        queryAvailability.setParameter("endDate",endDate);

        queryBook.setParameter("aparId",apartment.getApartmentId());
        queryBook.setParameter("startDate",startDate);
        queryBook.setParameter("endDate",endDate);

        List<Availability> availabilityResult = queryAvailability.getResultList();
        List<BookInfo> bookResult = queryBook.getResultList();

        if(availabilityResult.size()<1 || bookResult.size()>0){
            System.out.println("not available");
            System.out.println(apartment.toString());
            return false;
        }else{
            System.out.println("Available");
            System.out.println(apartment.toString());
            return true;
        }

    }

}