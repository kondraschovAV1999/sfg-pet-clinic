package learning.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class indexController {
    @RequestMapping({"", "/", "index.html"})
    public String index() {

        return "index";
    }

    @RequestMapping({"/oups"})
    public String error() {
        return "not_implemented";
    }
}
