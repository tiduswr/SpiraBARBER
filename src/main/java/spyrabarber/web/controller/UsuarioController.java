package spyrabarber.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spyrabarber.domain.Pessoa;
import spyrabarber.domain.Usuario;
import spyrabarber.service.PessoaService;
import spyrabarber.service.UsuarioService;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PessoaService pessoaService;

    @GetMapping("/list-all")
    public String listAllUsers(ModelMap map){
        map.addAttribute("users", usuarioService.listarTodosOsUsuarios());
        return "users/list-all";
    }

    @PostMapping("/save")
    public String saveUser(@Valid Usuario user, RedirectAttributes attr, BindingResult errors){

        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(e -> {
                attr.addFlashAttribute(e.getField(), e.getDefaultMessage());
            });
        }else{
            usuarioService.saveUser(user);
            attr.addFlashAttribute("sucesso", "Dados salvos com sucesso!");
        }
        attr.addFlashAttribute("user", user);
        return "redirect:/users/user-manager";
    }

    @PostMapping("/salvar-credenciais")
    public String saveCredenciais(@Valid Pessoa pessoa, RedirectAttributes attr, BindingResult errors){
        if(errors.hasErrors()){
            errors.getFieldErrors().forEach(e -> {
                attr.addFlashAttribute(e.getField(), e.getDefaultMessage());
            });
        }else{
            pessoa = pessoaService.atualizarUsuarioComDadosPessoais(pessoa);
            attr.addFlashAttribute("sucesso", "Dados salvos com sucesso!");
        }
        attr.addFlashAttribute("pessoa", pessoa);
        return "redirect:/users/credenciais-manager";
    }

    @GetMapping("/excluir/{id}")
    public String deleteUser(@PathVariable("id") Long id, RedirectAttributes attr, @AuthenticationPrincipal User user){
        usuarioService.deleteUser(id, user);
        attr.addFlashAttribute("sucesso", "Usuário excluido!");
        return "redirect:/users/list-all";
    }

    @GetMapping("/user-manager")
    public String manageUser(Usuario user, ModelMap map){
        if(!map.containsAttribute("user")) map.addAttribute("user", user);
        return "users/cadastro";
    }

    @GetMapping("/editar/{id}")
    public String preEditarUser(@PathVariable("id") Long id, RedirectAttributes attr){
        Usuario user = usuarioService.buscarPorId(id);
        attr.addFlashAttribute("user", user);
        return "redirect:/users/user-manager";
    }

    @GetMapping("/credenciais-manager")
    public String credenciaisUser(Pessoa pessoa, ModelMap map){
        if(!map.containsAttribute("pessoa")) map.addAttribute("pessoa", pessoa);
        return "users/pessoa-cadastro";
    }

    @GetMapping("/pre-editar-credenciais/{id}")
    public String preEditarDadosCredenciais(@PathVariable("id") Long id, RedirectAttributes attr){
        Pessoa pessoa = pessoaService.buscarPorUsuarioId(id);
        attr.addFlashAttribute("pessoa", pessoa);
        return "redirect:/users/credenciais-manager";
    }

}
