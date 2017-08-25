package application.basicControllers;

import application.database.Apartment;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
@ConfigurationProperties(prefix = "/profile/host")
public class HostController {

    @RequestMapping(value = "/add_apartment",method = RequestMethod.POST)
    String postAddApartmentController(Model model,
                                  @ModelAttribute Apartment formApartment
    ){
        System.out.println( formApartment.toString() );
        System.out.println(formApartment.getName());
        return "redirect:/";
    }

    @RequestMapping(value = "/add_apartment",method = RequestMethod.GET)
    String getAddApartmentController(Model model
    ){
        Apartment apartment = new Apartment();
        model.addAttribute("apartment",apartment);
        return "/add_apartment";
    }

    @RequestMapping(value = "/apartments",method = RequestMethod.GET)
    String getApartmetns(Model model){
        return "apartments";
    }
}
