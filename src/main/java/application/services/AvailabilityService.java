package application.services;


import application.database.Apartment;
import application.database.Availability;
import application.database.repositories.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.Query;
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
        query.setParameter("myDate1",availability.getToAv());
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

}