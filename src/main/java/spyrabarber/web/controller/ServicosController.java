package spyrabarber.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spyrabarber.domain.Servico;
import spyrabarber.domain.dto.ServicoDTO;
import spyrabarber.service.ServicosService;
import spyrabarber.web.exception.ImageManageException;
import spyrabarber.web.exception.NotUniqueNameException;
import spyrabarber.web.exception.ServicoHasDependencyException;

import javax.validation.Valid;

@Controller
@RequestMapping("/users/servicos")
public class ServicosController {

    @Autowired
    private ServicosService servicosService;

    @PostMapping("/save")
    public String saveServico(@Valid ServicoDTO dto, BindingResult errors, RedirectAttributes attr){

        if(errors.hasErrors()){
            errors.getFieldErrors().forEach( e ->
                    attr.addFlashAttribute(e.getField(), e.getDefaultMessage())
            );
        }else{
            try{
                servicosService.saveServico(dto);
                attr.addFlashAttribute("sucesso", "Serviço salvo com sucesso!");
            }catch(ImageManageException | NotUniqueNameException ex){
                if(ex instanceof ImageManageException){
                    ImageManageException image_exception = (ImageManageException) ex;
                    attr.addFlashAttribute(image_exception.getField(), image_exception.getMessage());
                }else {
                    NotUniqueNameException name_exception = (NotUniqueNameException) ex;
                    attr.addFlashAttribute(name_exception.getField(), name_exception.getMessage());
                }
                attr.addFlashAttribute("falha", "Erro ao salvar o serviço!");
            }
        }

        attr.addFlashAttribute("servicodto", dto);
        return "redirect:/users/servicos/manage";
    }

    @GetMapping("/manage")
    public String servicoManager(ServicoDTO dto, ModelMap map){
        if(!map.containsAttribute("servicodto")) map.addAttribute("servicodto", dto);
        map.addAttribute("servicos", servicosService.getAllServicos());
        return "users/servicos";
    }

    @GetMapping("/edit/{id}")
    public String editServico(@PathVariable("id") Long id, ModelMap map){
        map.addAttribute("servicodto", new ServicoDTO(servicosService.buscarPorId(id)));
        map.addAttribute("servicos", servicosService.getAllServicos());
        return "users/servicos";
    }

    @GetMapping("/delete/{id}")
    public String deleteServico(@PathVariable("id") Long id, RedirectAttributes attr){
        Servico s = servicosService.buscarPorId(id);
        try{
            servicosService.deleteServico(s);
            attr.addFlashAttribute("sucesso", "Serviço excluido com sucesso!");
        }catch(ImageManageException | ServicoHasDependencyException ex){
            if(ex instanceof ImageManageException){
                ImageManageException image_exception = (ImageManageException) ex;
                attr.addFlashAttribute(image_exception.getField(), image_exception.getMessage());
                attr.addFlashAttribute("falha", "Erro ao deletar servico");
            }else{
                ServicoHasDependencyException servico_exception = (ServicoHasDependencyException) ex;
                attr.addFlashAttribute("falha", servico_exception.getMessage());
            }
        }
        return "redirect:/users/servicos/manage";
    }

}
