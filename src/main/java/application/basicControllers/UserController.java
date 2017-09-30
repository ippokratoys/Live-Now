package application.basicControllers;

import application.Recommendation;
import application.database.*;
import application.database.repositories.*;
import application.services.ApartmentService;
import application.services.RecommendationService;
import application.services.ReviewService;
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
    ReviewService reviewService;

    @Autowired
    ApartmentService apartmentService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    LoginRepository loginRepository;

    @Autowired
    ApartmentRepository apartmentRepository;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    RecommendationService recommendationService;

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

    @RequestMapping(value = "/profile/user/rate",method = RequestMethod.POST)
    String submitRatingApartment(Model model,
                        @AuthenticationPrincipal final UserDetails userDetails,
                        @RequestParam(name = "rating") Double ratingBook,
                        @RequestParam(name = "book-id") int bookId,
                        @RequestParam(name = "content") String contentBook
//                                 @RequestParam(name="rating2") Double ratingHost,
//                                 @RequestParam(name="content2") Double contentHost
    ){
        if(userDetails==null){
            return "redirect:/login";
        }
        System.out.println("New rating : userDetails = [" + userDetails.getUsername() + "], rating = [" + ratingBook + "], bookId = [" + bookId + "], content = [" + contentBook + "]");
        BookInfo bookInfo= bookInfoRepository.findOne(bookId);
        Login login = loginRepository.findOne(userDetails.getUsername());
        if(bookInfo.getLogin().getUsername().equals(login.getUsername())){
//            short ratingToShort=ratingBook.shortValue();
            try {

                reviewService.createBookReview(bookId,contentBook,ratingBook,bookInfo.getApartment().getApartmentId());

            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/profile";
            }
            System.out.println("WAITING for the service");
        }else {
            System.out.println("this is not your book");
            return "redirect:/profile";
        }
        return "redirect:/profile/user/books?rating_done";
    }

    @RequestMapping(value = "/profile/user/rate_host",method = RequestMethod.POST)
    String submitRating(Model model,
                        @AuthenticationPrincipal final UserDetails userDetails,
                        @RequestParam(name = "rating") Double rating,
                        @RequestParam(name = "book-id") int bookId,
                        @RequestParam(name = "content") String content
    ){
        if(userDetails==null){
            return "redirect:/login";
        }
        System.out.println("New rating for host: userDetails = [" + userDetails.getUsername() + "], rating = [" + rating + "], bookId = [" + bookId + "], content = [" + content + "]");
        BookInfo bookInfo= bookInfoRepository.findOne(bookId);
        Login login = loginRepository.findOne(userDetails.getUsername());

        if(bookInfo.getLogin().getUsername().equals(login.getUsername())){
            short ratingToShort=rating.shortValue();
            try {
                reviewService.createHostReview(bookId,content,rating,userDetails.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/profile";
            }
            System.out.println("WAITING for the service");
        }else {
            System.out.println("this is not your book");
            return "redirect:/profile";
        }
        return "redirect:/profile/user/books?rating_done";
    }

    @Autowired
    Recommendation recommendation;
    @RequestMapping(value = "/rec")
        String recommendation(){
            recommendationService.recommendation("apostolos107");
//            System.out.println(recommendation.getVal());
            return "redirect:/";
        }
}