package spyrabarber.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import spyrabarber.service.UsuarioService;

@Controller
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/list-all")
    public String listAllUsers(ModelMap map){
        map.addAttribute("users", usuarioService.listarTodosOsUsuarios());
        return "/users/list-all";
    }
}
