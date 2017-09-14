package application.basicControllers;

import application.database.*;
import application.database.repositories.BookInfoRepository;
import application.database.repositories.ChatRepository;
import application.database.repositories.LoginRepository;
import application.database.repositories.MessageRepository;
import application.services.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.Date;
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
    MessageRepository messageRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @RequestMapping(value = "/profile/user/new_message/{apartment_id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Boolean newMessageToApartment(@AuthenticationPrincipal final UserDetails userDetails,
                       @PathVariable(value = "apartment_id") int apartmentId,
                       @RequestParam("message") String message
    ) {
        if (userDetails == null) {
            return false;
        }
        if (apartmentService.newMessageFromUser(message, userDetails.getUsername(), apartmentId) == false) {
            System.out.println("apartment returned false");
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping(value = "/profile/user/new_message_to_chat/{chat_id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Boolean newMessageToChat(@AuthenticationPrincipal final UserDetails userDetails,
                       @PathVariable(value = "chat_id") int chatId,
                       @RequestParam("message") String message
    ) {
        System.out.println(message);
        if (userDetails == null) {
            return false;
        }
        Chat chat = chatRepository.findOne(chatId);
        if(chat==null){
            System.out.println("chat not found");
            return false;
        }
        if (apartmentService.newMessageFromUser(message, userDetails.getUsername(), chat.getApartment().getApartmentId()) == false) {
            System.out.println("apartment returned false");
            return false;
        } else {
            return true;
        }
    }

    @RequestMapping(value = "/profile/user/chats")
    String getChatsPage(Model model,
                        @AuthenticationPrincipal final UserDetails userDetails
    ) {
        if (userDetails == null) {
            return "redirect:/";
        }
        Login login = loginRepository.findOne(userDetails.getUsername());
        List<Chat> chats = chatRepository.findAllByLoginOrderByChatIdDesc(login);

        model.addAttribute("chats", chats);
        return "user_chats";
    }

    @RequestMapping(value = "/profile/user/chat/messages/{chat_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Message> getChatMessages(@AuthenticationPrincipal final UserDetails userDetails,
                                  @PathVariable(value = "chat_id") int chatId
    ) {
        Chat chat = chatRepository.findOne(chatId);
        if(chat==null){
            System.out.println("not such a chat");
            return null;
        }
        if(! chat.getLogin().getUsername().equals(userDetails.getUsername())){
            System.out.println("not yours chat ");
            return null;
        }
        return messageRepository.findAllByChatOrderByMessageId(chat);
    }

    @RequestMapping("/profile/user/books")
    String getUserBooks(Model model,
                        @AuthenticationPrincipal final UserDetails userDetails
    ){
        if(userDetails==null){
            return "redirect:/login";
        }
        Login login = loginRepository.findOne(userDetails.getUsername());
        List<BookInfo> bookInfos=null;
        bookInfos = bookInfoRepository.findAllByLoginOrderByBookIn(login);
        System.out.println(bookInfos.size());
        model.addAttribute("allBooks",bookInfos);
        model.addAttribute("curDate",new Date());
        return "user_books";
    }
}