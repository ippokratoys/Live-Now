package application.basicControllers;

import application.database.Apartment;
import application.search.Result;
import application.search.Search;
import application.search.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by thanasis on 2/8/2017.
 */
@Controller
public class HomePage {
    SimpleDateFormat dateFormat=new SimpleDateFormat("MM/dd/yyy");
    {
        dateFormat=new SimpleDateFormat("MM/dd/yyy");
    }

    @Autowired
    SearchService searchService;

    @RequestMapping("/")
        String homeController(Model model
    ){
        return "index";
    }

    @RequestMapping("/result")
    String resultController(Model model,
                            @RequestParam("date-range") String dateRange,
                            @RequestParam("city") String city,
                            @RequestParam("people") Integer people,
                            @RequestParam(value = "max-cost",required = false,defaultValue = "0") Integer maxCost,
                            @RequestParam(value = "wifi",required = false, defaultValue = "false") Boolean wifi,
                            @RequestParam(value = "fridge",required = false, defaultValue = "false") Boolean fridge,
                            @RequestParam(value = "kitchen",required = false, defaultValue = "false") Boolean kitchen,
                            @RequestParam(value = "tv",required = false, defaultValue = "false") Boolean tv,
                            @RequestParam(value = "parking",required = false, defaultValue = "false") Boolean parking,
                            @RequestParam(value = "elevator",required = false, defaultValue = "false") Boolean elevator,
                            @RequestParam(value = "air-condition",required = false, defaultValue = "false") Boolean airCondition,
                            @RequestParam(value = "page",required = false, defaultValue = "1") Integer pageNum
    ){
        Date fromDate=null;
        Date toDate=null;
        String[] splitStr = dateRange.split("-");
        try {
            if(splitStr[0]!=null && splitStr[0]!=""){
                fromDate = dateFormat.parse(splitStr[0]);
            }else{
                return "index";
            }

            if(splitStr[1]!=null && splitStr[1]!=""){
                toDate = dateFormat.parse(splitStr[1]);
            }else{
                return "index";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "index";
        }
//        System.out.println("model = [" + model + "], dateRange = [" + dateRange + "], city = [" + city + "], people = [" + people + "], maxCost = [" + maxCost + "], wifi = [" + wifi + "], fridge = [" + fridge + "], kitchen = [" + kitchen + "], tv = [" + tv + "], parking = [" + parking + "], elevator = [" + elevator + "], airCondition = [" + airCondition + "]");

        Search filters = new Search(fromDate,toDate,city,people,wifi,fridge,kitchen,tv,parking,elevator,airCondition,"",maxCost);
        Result searchResults = searchService.getResultList(filters,pageNum);
        model.addAttribute("results",searchResults);

        model.addAttribute("oldDateStr",dateRange);
        model.addAttribute("oldValues",filters);
        return "result_page";
    }

    @RequestMapping("/hotel")
    String hotePageController(Model model,
                              @RequestParam(name = "hotel-id") int hotelId,
                              @RequestParam(name = "date-range",required = false,defaultValue = "") String dateRange,
                              @RequestParam(name = "people",required = false,defaultValue = "1") Integer people
    ){
        Date from = null;
        Date to = null;
        System.out.println("model = [" + model + "], hotelId = [" + hotelId + "], dateRange = [" + dateRange + "]");
        System.out.println(dateRange);
        if(dateRange!=null && !dateRange.trim().equals("")){
            String buff[] = dateRange.split("-") ;
            try {
                from = dateFormat.parse(buff[0]);
                to = dateFormat.parse(buff[1]);
            }catch (Exception e){
                e.printStackTrace();
                return "redirect:/";
            }
            model.addAttribute("dateRange",dateRange);
//            if(isHotelAvailable(from,to)==true){
            if(true){
                model.addAttribute("hotelIsBusy",false);
            }else{
                model.addAttribute("hotelIsBusy",true);
            }
        }
        model.addAttribute(people);
        return "hotel_page";
    }
}
