package application.basicControllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by thanasis on 23/8/2017.
 */

@Controller
public class GeneralUserControlers {

    @RequestMapping("/login")
    String getLogin(Model model){
        return "login";
    }

    @RequestMapping("/register")
    String getRegister(Model model){

        return "register";
    }

}
