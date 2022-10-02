package spyrabarber.web.exception;

public class UsuarioNotFoundException extends RuntimeException{
    public UsuarioNotFoundException(String m){
        super(m);
    }
}
