package application.basicControllers;

import application.database.repositories.LoginRepository;
import application.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    RegisterService registerService;

    @Autowired
    LoginRepository loginRepository;
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
        model.addAttribute("urlParams",allParams);
        System.out.println("ook come login");
        return "login";
    }


    @RequestMapping(value = "/register",method = RequestMethod.POST)
    String postRegister(Model model,
                       @RequestParam Map<String,String> allParams,
                       @AuthenticationPrincipal final UserDetails userDetails
    ){
        if(userDetails!=null){
            return "redirect:/";
        }
        try {
            registerService.createLogin(allParams);
        }catch (Exception e){
            if(e.getMessage().equals("User Exists")){
                model.addAttribute("error","user-exists");
                model.addAttribute("oldVal",allParams);
                return "register";
            }else if(e.getMessage().equals("Passwords do not match")){
                model.addAttribute("oldVal",allParams);
                model.addAttribute("error","password-match");
                return "register";
            }else{
                model.addAttribute("error","other");
                model.addAttribute("oldVal",allParams);
                e.printStackTrace();
                return "register";
            }
        }
        System.out.println("Done");
        return  "redirect:login?login=successful";
    }

    @RequestMapping("/register")
    String getRegister(Model model,
                       @RequestParam Map<String,String> allParams,
                       @AuthenticationPrincipal final UserDetails userDetails
                       ){
        if(userDetails!=null){
            return "redirect:/";
        }
        return "register";
    }

    @RequestMapping("/profile")
    String getProfile(Model model,
                      @AuthenticationPrincipal final UserDetails userDetails
    ){
        if(userDetails==null){
            return "redirect:/login";
        }
        model.addAttribute("user",loginRepository.findOne(userDetails.getUsername()));
        return "profile";
    }

}
