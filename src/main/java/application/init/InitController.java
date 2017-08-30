package application.init;

import application.database.initializer.CsvInserts;
import application.database.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by thanasis on 31/8/2017.
 */
@Controller
public class InitController {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ApartmentRepository apartmentRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    AvailabilityRepository availabilityRepository;
    @Autowired
    BookInfoRepository bookInfoRepository;

    @Autowired
    CsvInserts csvInserts;

    @RequestMapping("/init/all")
    String initAll(){
        csvInserts.loginCsvInsertions("csv/login.csv",loginRepository);
        csvInserts.userRoleCsvInsertions("csv/user_role.csv",loginRepository,userRoleRepository);
        csvInserts.apartmentCsvInsertions("csv/apartment.csv",apartmentRepository,loginRepository);
        csvInserts.availabilityCsvInsertions("csv/availability.csv",availabilityRepository,apartmentRepository);
        csvInserts.bookInfoCsvInsertions("csv/book_info.csv",bookInfoRepository,apartmentRepository,loginRepository);
        return "redirect:/";
    }

}
