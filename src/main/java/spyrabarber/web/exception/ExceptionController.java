package spyrabarber.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ModelAndView usuarioNaoEncontradoExcessao(UsuarioNotFoundException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.NOT_FOUND.value());
        error.addObject("errorTitle", "Usuário não encontrado");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

    @ExceptionHandler(SelfExclusionException.class)
    public ModelAndView tentativaDeAutoExclusaoExcessao(SelfExclusionException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.FORBIDDEN.value());
        error.addObject("errorTitle", "Tentativa de auto exclusão");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

    @ExceptionHandler(ClienteHasMoreThanOnePerfilException.class)
    public ModelAndView clientePossuiMaisDeUmPerfilExcessao(ClienteHasMoreThanOnePerfilException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.FORBIDDEN.value());
        error.addObject("errorTitle", "Cliente com mais de um perfil");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

}
