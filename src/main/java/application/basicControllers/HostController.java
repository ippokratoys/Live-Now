package application.basicControllers;

import application.database.Apartment;
import application.database.Availability;
import application.database.Chat;
import application.database.Message;
import application.database.repositories.ApartmentRepository;
import application.database.repositories.ChatRepository;
import application.database.repositories.LoginRepository;
import application.services.ApartmentService;
import application.services.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by thanasis on 23/8/2017.
 */
@Controller
public class HostController {
    SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyy");

    @Autowired
    private ChatRepository chatRepository;

    {
        dateFormat=new SimpleDateFormat("MM/dd/yyy");
    }

    @Autowired
    AvailabilityService availabilityService;
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    ApartmentService apartmentService;
    @Autowired
    private ApartmentRepository apartmentRepository;

    @RequestMapping(value = "/profile/host/add_apartment",method = RequestMethod.POST)
    String postAddApartmentController(Model model,
                                  @ModelAttribute Apartment formApartment,
                                  @AuthenticationPrincipal final UserDetails userDetails

    ){
        System.out.println( formApartment.toString() );
        System.out.println("Creating Hotel " +formApartment.getName());
        try {
            apartmentService.createApartment(loginRepository.findOne(userDetails.getUsername()),formApartment);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
        return "redirect:/profile";
    }

    @RequestMapping(value = "/profile/host/add_apartment",method = RequestMethod.GET)
    String getAddApartmentController(Model model,
                                     @AuthenticationPrincipal final UserDetails userDetails
    ){
        Apartment apartment = new Apartment();
        model.addAttribute("apartment",apartment);
        return "/add_apartment";
    }

    @RequestMapping(value = "/profile/host/apartments",method = RequestMethod.GET)
    String getApartmetns(Model model,
                         @AuthenticationPrincipal final UserDetails userDetails
    ){
        if(userDetails==null){
            return "redirect:/login";
        }
        List<Apartment> apartments = loginRepository.findOne(userDetails.getUsername()).getApartments();

//        we add this just to use the form for update(just in case)
        Apartment apartment = new Apartment();
        model.addAttribute("apartment",apartment);

        model.addAttribute("apartments",apartments);
        return "apartments";
    }

    @RequestMapping(value = "/profile/host/apartment/dates/{apartment_id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Availability> getExistingDates(@AuthenticationPrincipal final UserDetails userDetails,
                                        @PathVariable(value = "apartment_id") int apartmentId

    ){
        if(userDetails==null){
            System.out.println("it's null");
            return null;
        }else {
            System.out.println(userDetails.getUsername());
        }
        Apartment apartment = apartmentRepository.findOne(apartmentId);
        if(apartment==null){
            System.out.println("No apartment with id "+apartmentId);
            return null;
        }
        System.out.println(apartment.getAvailabilities().size());
        return apartment.getAvailabilities();
    }

    @RequestMapping(value = "profile/host/apartment/dates/{apartment_id}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Boolean addDate(@AuthenticationPrincipal final UserDetails userDetails,
                   @PathVariable(value = "apartment_id") int apartmentId,
                   @RequestParam("date_range") String dateStr
    ){
        if(userDetails==null){
            return false;
        }
        System.out.println("userDetails = [" + userDetails + "], apartmentId = [" + apartmentId + "], dateStr = [" + dateStr + "]");
        Date from = null;
        Date to = null;
        if(dateStr==null || dateStr.trim().equals("")){
            System.out.println("Date not exists");
        }
        String buff[] = dateStr.split("-");
        try{
            from=dateFormat.parse(buff[0]);
            to=dateFormat.parse(buff[1]);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        try{
            Apartment apartment = apartmentRepository.findOne(apartmentId);
            Availability availability = new Availability(from,to,apartment);
            if( !apartment.getLogin().getUsername().equals(userDetails.getUsername()) ){
                System.out.println("not yours hotel "+ userDetails.getUsername());
                return false;
            }
            availabilityService.createAvailability(apartment,availability);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/profile/host/apartment/{apartment_id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Apartment getApartmentInfo(@AuthenticationPrincipal final UserDetails userDetails,
                                @PathVariable(value = "apartment_id") int apartmentId

    ){
        Apartment apartment = apartmentRepository.findOne(apartmentId);
        if(apartment==null){
            System.out.println("No apartment with id "+apartmentId);
            return null;
        }
        return apartment;
    }

    @RequestMapping(value = "/profile/host/apartment/{apartment_id}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Boolean editApartmentInfo(@AuthenticationPrincipal final UserDetails userDetails,
                              @PathVariable(value = "apartment_id") int apartmentId,
                              @ModelAttribute Apartment formApartment
    ){
        System.out.println("-----------------------");
        System.out.println(formApartment.toString());
        try{
            if(apartmentService.editApartment(formApartment.getApartmentId(),formApartment)==false){
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        System.out.println("-----------------------");
        return true;
    }

    @RequestMapping(value = "/profile/host/apartment/chats/{apartment_id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Chat> getApartmentChat(@AuthenticationPrincipal final UserDetails userDetails,
                                @PathVariable(value = "apartment_id") int apartmentId
    ){
        if(apartmentService.authentication(userDetails,apartmentId)==false){
            System.out.println("not your apartment "+userDetails.toString()+"|||"+apartmentId);
            return null;
        }
        Apartment apartment = apartmentRepository.findOne(apartmentId);
        if(apartment==null){
            return null;
        }
        return apartment.getChats();
    }

    @RequestMapping(value = "/profile/host/chats/messages/{chat_id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<Message> getChatMessages(@AuthenticationPrincipal final UserDetails userDetails,
                                   @PathVariable(value = "chat_id") int chatId
    ){
        Chat chat=chatRepository.findOne(chatId);
        if(chat==null){
            return null;
        }
        if(apartmentService.authentication(userDetails,chat.getApartment())==false){
            System.out.println("not your chat "+userDetails.toString()+"|||"+chatId);
            return null;
        }
        return chat.getMessages();
    }

    @RequestMapping(value = "/profile/host/chats/messages/{chat_id}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Boolean newChatMessage(@AuthenticationPrincipal final UserDetails userDetails,
                           @PathVariable(value = "chat_id") int chatId,
                           @RequestParam("message") String message
    ){
        Chat chat=chatRepository.findOne(chatId);
        if(chat==null){
            return false;
        }
        if(apartmentService.authentication(userDetails,chat.getApartment())==false){
            System.out.println("not your chat "+userDetails.toString()+"|||"+chatId);
            return false;
        }

        if(apartmentService.newMessageFormHost(message,chatId)==false){
            System.out.println("the service not OK");
            return false;
        }else{
            return true;
        }

    }

}
