package application.basicControllers;

import application.database.Apartment;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by thanasis on 23/8/2017.
 */
@Controller()
public class HostController {
    @Autowired
    LoginRepository loginRepository;

    @RequestMapping(value = "/profile/host/add_apartment",method = RequestMethod.POST)
    String postAddApartmentController(Model model,
                                  @ModelAttribute Apartment formApartment,
                                  @AuthenticationPrincipal final UserDetails userDetails

    ){
        System.out.println( formApartment.toString() );
        System.out.println(formApartment.getName());
        return "redirect:/";
    }

    @RequestMapping(value = "/profile/host/add_apartment",method = RequestMethod.GET)
    String getAddApartmentController(Model model,
                                     @AuthenticationPrincipal final UserDetails userDetails
    ){
        Apartment apartment = new Apartment();
        model.addAttribute("apartment",apartment);
        return "/add_apartment";
    }

    @RequestMapping(value = "/profile/host/apartments",method = RequestMethod.GET)
    String getApartmetns(Model model,
                         @AuthenticationPrincipal final UserDetails userDetails
                         ){
        List<Apartment> apartments = loginRepository.findOne(userDetails.getUsername()).getApartments();
        model.addAttribute("apartments",apartments);
        return "apartments";
    }
}
