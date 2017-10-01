package application.init;

import application.database.Apartment;
import application.database.Availability;
import application.database.Login;
import application.database.UserRole;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.AvailabilityRepository;
import application.database.repositories.LoginRepository;
import application.database.repositories.UserRoleRepository;
import org.hibernate.resource.jdbc.LogicalConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static application.init.CsvReader.parseLine;

/**
 * Created by thanasis on 1/10/2017.
 */
@Service
public class ApartmentCsvInsert{
    private static final int COL_ID = 0;
    private static final int COL_URL= 1;
    private static final int COL_NAME= 4;
    private static final int COL_COUNTRY= 43;
    private static final int COL_LAT= 44;
    private static final int COL_LON= 45;
    private static final int COL_CITY= 38;
    private static final int COL_PRICE= 56;
    private static final int COL_CLEAN_PRICE= 60;
    private static final int COL_EXTRA_PPL= 62;

    @Autowired
    ApartmentRepository apartmentRepository;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    AvailabilityRepository availabilityRepository;

    public ApartmentCsvInsert() {
    }

    public void initApartment() throws Exception{
        Login login = loginRepository.findOne("airbnb");
        if(login==null){
            login = new Login();
            login.setUsername("airbnb");
            login.setPassword("1234");
            login.setName("Kid");
            login.setSurname("Rich");
            login.setEmail("air@bn.com");
            login.setPhoneNum("+302106000000");
            login.setEnabled(true);
            login.setPhotoPath("UsersPhotos/none");
            loginRepository.save(login);
            UserRole userRole=new UserRole();
            userRole.setLogin(login);
            userRole.setRole("host");
            userRoleRepository.save(userRole);
        }
        String csvFile = "csv/listings.csv";
        Scanner scanner = new Scanner(new File(csvFile));
        int readUntilNow=0;
        while (scanner.hasNext()){
            Apartment apartment;
            List<String> line = parseLine(scanner.nextLine());
            readUntilNow++;
            System.out.print(line.get(COL_ID)+" "+line.get(COL_NAME).trim().replace("\"",""));
            System.out.print(" City "+line.get(COL_CITY));
            System.out.print(" LAT "+line.get(COL_LAT));
            System.out.print(" LON "+line.get(COL_LON));
            System.out.println(" Price "+line.get(COL_PRICE).replace("\"$",""));
            System.out.println("    line "+readUntilNow);
            if(readUntilNow==1){
                continue;
            }

            apartment = new Apartment();
//            apartment.setApartmentId(Integer.parseInt(line.get(COL_ID)));
            apartment.setName(line.get(COL_NAME).trim().replace("\"",""));
            apartment.setCountry(line.get(COL_COUNTRY).trim().replace("\"",""));
            apartment.setLat(new BigDecimal(line.get(COL_LAT)));
            apartment.setLon(new BigDecimal(line.get(COL_LON)));
            apartment.setLocality(line.get(COL_CITY).replaceAll("\"",""));
            apartment.setMinPeople((short) 0);
            apartment.setStandardPeople((short) 2);
            apartment.setMaxPeople((short) 7);
            if(readUntilNow%3==0)
                apartment.setType("whole_apartment");
            if(readUntilNow%3==1)
                apartment.setType("private_room");
            if(readUntilNow%3==2)
                apartment.setType("shared_room");
            apartment.setPrice(((short) Double.parseDouble(line.get(COL_PRICE).replace("\"$",""))));
//            apartment.setCleanPrice(((short) Double.parseDouble(line.get(COL_CLEAN_PRICE).replace("\"$",""))));
//            apartment.setPlusPrice(((short) Double.parseDouble(line.get(COL_EXTRA_PPL).replace("\"$",""))));
            apartment.setLogin(login);

            apartmentRepository.save(apartment);
            apartmentRepository.findOne(apartment.getApartmentId());
            Availability availability = new Availability();
            availability.setApartment(apartment);
            availability.setFromAv(new Date(1990,1,1));
            availability.setFromAv(new Date(2100,1,1));
            availabilityRepository.save(availability);
            if(readUntilNow==100)break;
        }
    }

}
