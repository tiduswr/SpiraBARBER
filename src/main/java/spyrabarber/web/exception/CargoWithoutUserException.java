package spyrabarber.web.exception;

public class CargoWithoutUserException extends RuntimeException{
    public CargoWithoutUserException(String m){
        super(m);
    }
}
