package application.basicControllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by thanasis on 2/8/2017.
 */
@Controller
public class HomePage {

    @RequestMapping("/")
    String homeController(Model model){
        return "index";
    }
}
