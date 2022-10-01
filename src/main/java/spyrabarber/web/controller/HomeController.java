package spyrabarber.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String home(){
        return "/index";
    }

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @GetMapping({"/login-error"})
    public String loginError(ModelMap map){
        map.addAttribute("alerta", "erro");
        map.addAttribute("titulo", "Credenciais Inválidas!");
        map.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
        return "/login";
    }

}