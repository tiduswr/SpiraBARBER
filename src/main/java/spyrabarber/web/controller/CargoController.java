package spyrabarber.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import spyrabarber.domain.dto.CargoDTO;
import spyrabarber.service.CargoService;
import spyrabarber.web.exception.CargoWithoutUserException;

import javax.validation.Valid;

@Controller
@RequestMapping("/users/cargos")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping("/cargo-manager")
    public String manageUserCargo(CargoDTO cargoDTO, ModelMap map) throws CargoWithoutUserException{
        if(!map.containsAttribute("cargodto"))
            throw new CargoWithoutUserException("Não é possivel completar a operação");
        map.addAttribute("cargos", cargoService.buscarTodosOsCargos());
        return "/users/cargos";
    }

    @GetMapping("/editarcargo/byid/{id}")
    public String preEditarCargos(@PathVariable("id") Long id, RedirectAttributes attr){
        CargoDTO cargoDTO = cargoService.montarCargoDTOByUser(id);
        attr.addFlashAttribute("cargodto", cargoDTO);
        return "redirect:/users/cargos/cargo-manager";
    }

    @PostMapping("/save")
    public String saveCargo(@Valid CargoDTO cargoDTO, RedirectAttributes attr, BindingResult errors){
        if(errors.hasErrors()){
            configErrorFields(errors, attr);
        }else{
            if(cargoService.atualizarCargo(cargoDTO, errors)){
                attr.addFlashAttribute("sucesso", "Cargo atualizado!");
            }else{
                configErrorFields(errors, attr);
                attr.addFlashAttribute("falha", "Um erro ocorreu ao tentar atualizar o cargo");
            }
        }
        cargoDTO = cargoService.montarCargoDTOByUser(cargoDTO.getUser().getId());
        attr.addFlashAttribute("cargodto", cargoDTO);
        return "redirect:/users/cargos/cargo-manager";
    }

    private void configErrorFields(BindingResult errors, RedirectAttributes attr){
        errors.getFieldErrors().forEach(e ->{
            attr.addFlashAttribute(e.getField(), e.getDefaultMessage());
        });
    }

}
