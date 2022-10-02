package spyrabarber.web.exception;

public class ClienteHasMoreThanOnePerfilException extends RuntimeException{
    public ClienteHasMoreThanOnePerfilException(String m){
        super(m);
    }
}
