package application.basicControllers;

import application.database.Apartment;
import application.database.Chat;
import application.database.Login;
import application.database.repositories.ChatRepository;
import application.database.repositories.LoginRepository;
import application.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by thanasis on 25/8/2017.
 */
@Controller
public class UserController {

    @Autowired
    ApartmentService apartmentService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    LoginRepository loginRepository;

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
    @RequestMapping(value = "/profile/user/chats")
    String getChatsPage(Model model,
                        @AuthenticationPrincipal final UserDetails userDetails
    ){
        if(userDetails==null){
            return "redirect:/";
        }
        Login login = loginRepository.findOne(userDetails.getUsername());
        List<Chat> chats=chatRepository.findAllByLoginOrderByChatIdDesc(login);

        model.addAttribute("chats",chats);
        return "user_chats";
    }
}
