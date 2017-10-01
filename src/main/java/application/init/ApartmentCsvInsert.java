package application.init;

import application.database.Apartment;
import application.database.Availability;
import application.database.Login;
import application.database.UserRole;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.AvailabilityRepository;
import application.database.repositories.LoginRepository;
import application.database.repositories.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;
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

    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    {
        format = new SimpleDateFormat("dd/MM/yyyy");
    }


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
        Random random = new Random(11);
        while (scanner.hasNext()){
            Apartment apartment;
            List<String> line = parseLine(scanner.nextLine());
            readUntilNow++;
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
            apartment.setCleanPrice((short) (random.nextInt(50)%20));
            apartment.setPlusPrice(((short) (random.nextInt()%20)));
            apartment.setLogin(login);

            apartmentRepository.save(apartment);
            apartmentRepository.findOne(apartment.getApartmentId());
            Availability availability = new Availability();
            availability.setApartment(apartment);
            availability.setFromAv(format.parse("22/10/1996"));
            availability.setToAv(format.parse("22/10/2020"));

            availabilityRepository.save(availability);
            if(readUntilNow==100)break;
        }
    }

}
