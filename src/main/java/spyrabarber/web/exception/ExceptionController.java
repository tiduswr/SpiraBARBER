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
        error.addObject("error", "O usuário não foi encontrado!");
        error.addObject("message", e.getMessage());
        return error;
    }

}
