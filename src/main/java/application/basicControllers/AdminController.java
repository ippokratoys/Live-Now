package application.basicControllers;

import application.database.*;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.LoginRepository;
import application.services.ApartmentService;
import application.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ApartmentService apartmentService;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    RegisterService registerService;

    @RequestMapping(value = "/profile/admin",method = RequestMethod.GET)
    String getLogins(Model model,
                         @AuthenticationPrincipal final UserDetails userDetails
    ){

        if(userDetails==null){
            return "redirect:/login";
        }
        model.addAttribute("users",loginRepository.findAll());
        System.out.println("-----------");
        for (Login oneUser :
                loginRepository.findAll()) {
            System.out.println(oneUser.getUsername()+": host="+ ( registerService.isHost(oneUser.getUsername())).toString() +"\t "+"user " +registerService.isUser(oneUser.getUsername()));
        }
        System.out.println("-----------");
        return "admin";
    }

    @RequestMapping("profile/admin/userInfo")
    String getUserInfo(Model model,
                       @RequestParam(name="username")String username,
                       @AuthenticationPrincipal final UserDetails userDetails
    ){
        if(userDetails==null){
            return "redirect:/login";
        }
        Login login=loginRepository.findOne(username);
        List<UserRole> userRoles=login.getUserRoles();
        model.addAttribute("user",login);
        model.addAttribute("userRole",userRoles);
        for(UserRole userRole:userRoles){
            if(userRole.getRole()=="host"){
                List<Apartment> apartments=login.getApartments();
                model.addAttribute("apartments",apartments);
                break;
            }
        }
        return "user_info";
    }

    @RequestMapping("profile/admin/acceptUser")
    String acceptHost(@RequestParam(name="username")String username
                      ){
        System.out.println("Accept "+username);
        Login login=loginRepository.findOne(username);
        login.setEnabled(true);
        loginRepository.save(login);
        return "redirect:/profile/admin";
    }

    @RequestMapping("profile/admin/XML")
    public ResponseEntity<byte[]>createXml(@RequestParam(name="apartmentId")int apartmentId
    ){
        String theFile = apartmentService.createXml(apartmentId);
        byte bytes[]= theFile.getBytes();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/xml"));
        String filename = "apartment_"+apartmentRepository.findOne(apartmentId).getName()+".xml";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);
        return  response;
    }
}
