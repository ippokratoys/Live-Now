package application.basicControllers;

import application.database.Apartment;
import application.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by thanasis on 25/8/2017.
 */
@Controller
public class UserController {

    @Autowired
    ApartmentService apartmentService;

    @RequestMapping(value = "/profile/user/new_message/{apartment_id}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Boolean newMessage(@AuthenticationPrincipal final UserDetails userDetails,
                       @PathVariable(value = "apartment_id") int apartmentId,
                       @RequestParam("message") String message
    ){
        if(userDetails==null){
            return false;
        }
        if(apartmentService.newMessageFromUser(message,userDetails.getUsername(),apartmentId)==false){
            System.out.println("apartment returned false");
            return false;
        }else {
            return true;
        }
    }
}
