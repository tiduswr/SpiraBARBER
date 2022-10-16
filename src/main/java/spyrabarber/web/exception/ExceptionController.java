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
        error.addObject("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        error.addObject("errorTitle", "Tentativa de auto exclusão");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

    @ExceptionHandler(ClienteHasMoreThanOnePerfilException.class)
    public ModelAndView clientePossuiMaisDeUmPerfilExcessao(ClienteHasMoreThanOnePerfilException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        error.addObject("errorTitle", "Cliente com mais de um perfil");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

    @ExceptionHandler(HasCargosException.class)
    public ModelAndView possuiCargosNoVinculadosExcessao(HasCargosException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        error.addObject("errorTitle", "Essa pessoa possui histórico de cargos");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

    @ExceptionHandler(HasServicosException.class)
    public ModelAndView possuiServicosVinculadosExcessao(HasServicosException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        error.addObject("errorTitle", "Possui Serviços");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

    @ExceptionHandler(CargoWithoutUserException.class)
    public ModelAndView cargoSemUsuarioException(CargoWithoutUserException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        error.addObject("errorTitle", e.getMessage());
        error.addObject("errorDescription", "Você não pode acessar essa area sem um usuário para associar o cargo");
        return error;
    }

    @ExceptionHandler(UserHasNotDataToBeActiveException.class)
    public ModelAndView cadastroIncompletoExcessao(UserHasNotDataToBeActiveException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.METHOD_NOT_ALLOWED.value());
        error.addObject("errorTitle", "Cadastro Incompleto");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

    @ExceptionHandler(ServicoNotFoundException.class)
    public ModelAndView servicoNaoEncontradoExcessao(ServicoNotFoundException e){
        ModelAndView error = new ModelAndView("error");
        error.addObject("status", HttpStatus.NOT_FOUND.value());
        error.addObject("errorTitle", "Não encontrado");
        error.addObject("errorDescription", e.getMessage());
        return error;
    }

}
