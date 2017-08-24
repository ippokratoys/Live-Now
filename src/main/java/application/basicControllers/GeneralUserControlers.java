package application.basicControllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Created by thanasis on 23/8/2017.
 */

@Controller
public class GeneralUserControlers {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    String getLogin(@RequestParam Map<String,String> allParams,
                    @AuthenticationPrincipal final UserDetails userDetails,
                    Model model
    ){
        System.out.println(allParams);
        System.out.println(allParams.get("error"));

        if(userDetails!=null && allParams.get("logout")==null ){
            System.out.println(userDetails);
            return "redirect:/";
        }
        System.out.println("ook come login");
        return "login";
    }

    @RequestMapping("/register")
    String getRegister(Model model){

        return "register";
    }

}
