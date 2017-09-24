package application.basicControllers;

import application.database.Apartment;
import application.database.Login;
import application.database.UserRole;
import application.database.repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    LoginRepository loginRepository;


    @RequestMapping(value = "/profile/admin",method = RequestMethod.GET)
    String getLogins(Model model,
                         @AuthenticationPrincipal final UserDetails userDetails
    ){

        if(userDetails==null){
            return "redirect:/login";
        }
        Iterable<Login> users=loginRepository.findAll();
        model.addAttribute("users",users);
        return "users";
    }

    @RequestMapping("/userInfo")
    String getUserInfo(Model model,
                       @RequestParam(name="username")String username,
                       @AuthenticationPrincipal final UserDetails userDetails
    ){
        if(userDetails==null){
            return "redirect:/login";
        }
        Login login=loginRepository.findOne(username);
        List<UserRole> userRoles=login.getUserRoles();
        model.addAttribute("userInfo",login);
        model.addAttribute("userRole",userRoles);
        for(UserRole userRole:userRoles){
            if(userRole.getRole()=="host"){
                List<Apartment> apartments=login.getApartments();
                model.addAttribute("apartments",apartments);
                break;
            }
        }
        return "userInfo";
    }
}
