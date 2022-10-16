package spyrabarber.web.exception;

public class NotUniqueNameException extends FieldException{
    public NotUniqueNameException(String m, String f){
        super(m, f);
    }
}
