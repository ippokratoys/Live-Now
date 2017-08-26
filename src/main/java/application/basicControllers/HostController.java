package application.basicControllers;

import application.database.Apartment;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by thanasis on 23/8/2017.
 */
@Controller()
public class HostController {

    @RequestMapping(value = "/profile/host/add_apartment",method = RequestMethod.POST)
    String postAddApartmentController(Model model,
                                  @ModelAttribute Apartment formApartment
    ){
        System.out.println( formApartment.toString() );
        System.out.println(formApartment.getName());
        return "redirect:/";
    }

    @RequestMapping(value = "/profile/host/add_apartment",method = RequestMethod.GET)
    String getAddApartmentController(Model model
    ){
        Apartment apartment = new Apartment();
        model.addAttribute("apartment",apartment);
        return "/add_apartment";
    }

    @RequestMapping(value = "/profile/host/apartments",method = RequestMethod.GET)
    String getApartmetns(Model model){
        return "apartments";
    }
}
