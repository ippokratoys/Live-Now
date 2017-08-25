package application.basicControllers;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;

/**
 * Created by thanasis on 25/8/2017.
 */
@Controller
@ConfigurationProperties(prefix = "/profile/user")
public class UserController {

}
